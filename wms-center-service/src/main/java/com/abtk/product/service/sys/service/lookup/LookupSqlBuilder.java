package com.abtk.product.service.sys.service.lookup;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.mapper.ColumnMetaMapper;
import com.abtk.product.dao.mapper.TableMetaMapper;
import com.abtk.product.dao.support.lookup.LookupColumn;
import com.abtk.product.dao.util.SqlInjectionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Lookup 虚拟列 SQL 构造器。
 *
 * <p>职责：</p>
 * <ul>
 *   <li>根据 sys_column_meta 扫描出 tableCode 下所有"虚拟联表列"（ref_table_code 非空）</li>
 *   <li>对每个虚拟列做 <b>表级 + 字段级 双层白名单校验</b></li>
 *   <li>产出 {@link LookupColumn} 列表，供 {@code CrudServiceImpl} 直接拼 SQL，杜绝二次注入风险</li>
 * </ul>
 *
 * <p>安全策略：</p>
 * <ul>
 *   <li>refTableCode 必须在 sys_table_meta 中存在（通过 {@link SqlInjectionValidator#validateTableFromMeta}）</li>
 *   <li>refMatchField / refTargetField / refLocalField 必须通过 {@link SqlInjectionValidator#validateFieldFormat}</li>
 *   <li>refMatchField / refTargetField 若 sys_column_meta(refTableCode) 非空，则要求必须命中；否则降级为仅格式校验</li>
 *   <li>虚拟列 field 别名 不能与主表物理列重名（调用侧在执行前二次校验）</li>
 * </ul>
 *
 * <p>统一约定：所有表的逻辑删除列均为 {@code is_deleted}（与 CrudServiceImpl.DEFAULT_DELETE_COLUMN 保持一致）。</p>
 */
@Slf4j
@Component
public class LookupSqlBuilder {

    /** 虚拟列存在时使用的固定 JOIN 别名前缀 */
    private static final String JOIN_ALIAS_PREFIX = "j";

    /** 约定：所有表逻辑删除列 */
    private static final String DEFAULT_DELETE_COLUMN = "is_deleted";

    /** 驼峰转 snake_case 用 */
    private static final Pattern CAMEL_PATTERN = Pattern.compile("([a-z0-9])([A-Z])");

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Autowired
    private TableMetaMapper tableMetaMapper;

    /**
     * 扫描指定表的字段元数据，构造已校验的 Lookup 列表。
     *
     * @param tableCode 主表编码
     * @return 已校验的 LookupColumn 列表（顺序按 sort_order 稳定），空列表代表无虚拟列
     */
    public List<LookupColumn> buildForTable(String tableCode) {
        if (tableCode == null || tableCode.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<ColumnMeta> columns = columnMetaMapper.selectByTableCode(tableCode);
        if (columns == null || columns.isEmpty()) {
            return new ArrayList<>();
        }
        List<LookupColumn> lookups = new ArrayList<>();
        int aliasIdx = 1;
        for (ColumnMeta col : columns) {
            if (!isLookupMeta(col)) {
                continue;
            }
            LookupColumn lookup = buildOne(col, aliasIdx);
            lookups.add(lookup);
            aliasIdx++;
        }
        return lookups;
    }

    /**
     * 将 WHERE 搜索参数中的虚拟字段 key 改写为 SQL 表达式。
     *
     * <p>输入参数是 Service 层已经做过 snake_case 归一化的 Map（key 全部是 snake_case）。
     * 返回：将所有属于虚拟列的 key 拆分到单独的 virtualParams Map，并附带 sqlExpression 映射。</p>
     *
     * <p>示例：</p>
     * <pre>
     * 输入  mainParams: {location_no=A, warehouse_name=主仓}
     * 输出
     *   mainParams(更新后): {location_no=A}
     *   virtualParams:      {warehouse_name=主仓}
     *   virtualExpressions: {warehouse_name=j1.warehouse_name}
     * </pre>
     *
     * @param mainParams 主表参数（原地修改，移除虚拟列参数）
     * @param lookups LookupColumn 列表
     * @return VirtualParamHolder，可直接丢给 MyBatis
     */
    public VirtualParamHolder splitVirtualParams(Map<String, Object> mainParams, List<LookupColumn> lookups) {
        VirtualParamHolder holder = new VirtualParamHolder();
        if (mainParams == null || mainParams.isEmpty() || lookups == null || lookups.isEmpty()) {
            return holder;
        }
        Map<String, String> virtualFieldToExpr = new HashMap<>();
        for (LookupColumn lk : lookups) {
            // 虚拟列 field 的 snake_case 形式（来源于 column_meta.field 驼峰 → snake）
            virtualFieldToExpr.put(lk.getAliasField(), lk.getSqlExpression());
        }
        // 不用 removeIf，避免 ConcurrentModificationException
        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, Object> e : mainParams.entrySet()) {
            String key = e.getKey();
            if (virtualFieldToExpr.containsKey(key)) {
                holder.getValues().put(key, e.getValue());
                holder.getSqlExpressions().put(key, virtualFieldToExpr.get(key));
                toRemove.add(key);
            }
        }
        for (String k : toRemove) {
            mainParams.remove(k);
        }
        return holder;
    }

    /**
     * 将排序字段（snake_case 或 camelCase）解析成最终 SQL 片段。
     * <ul>
     *   <li>虚拟列 → "j1.warehouse_name"</li>
     *   <li>主表列 → "m.warehouse_code"</li>
     *   <li>null/空 → null</li>
     * </ul>
     */
    public String resolveOrderByExpression(String orderBySnakeField, List<LookupColumn> lookups) {
        if (orderBySnakeField == null || orderBySnakeField.trim().isEmpty()) {
            return null;
        }
        String normalized = orderBySnakeField.trim();
        if (lookups != null) {
            for (LookupColumn lk : lookups) {
                if (lk.getAliasField().equalsIgnoreCase(normalized)) {
                    return lk.getSqlExpression();
                }
            }
        }
        return "m." + normalized;
    }

    /** 判断一条 column_meta 是否是虚拟联表列 */
    public static boolean isLookupMeta(ColumnMeta col) {
        return col != null
                && isNotBlank(col.getRefTableCode())
                && isNotBlank(col.getRefMatchField())
                && isNotBlank(col.getRefTargetField());
    }

    private LookupColumn buildOne(ColumnMeta col, int aliasIdx) {
        String refTable = col.getRefTableCode().trim();
        String matchField = col.getRefMatchField().trim();
        String rawTargetField = col.getRefTargetField().trim();
        String localField = isNotBlank(col.getRefLocalField())
                ? col.getRefLocalField().trim()
                : camelToSnake(col.getField());

        // 表级白名单（先格式再 sys_table_meta 存在性）
        SqlInjectionValidator.validateTableFormat(refTable);
        SqlInjectionValidator.validateTableFromMeta(refTable, tableMetaMapper);

        // 字段级格式校验（强制）
        SqlInjectionValidator.validateFieldFormat(matchField);
        SqlInjectionValidator.validateFieldFormat(localField);

        // WMS-LOWCODE-LOOKUP-CONCAT：支持多字段拼接，按逗号拆分并逐个校验
        List<String> targetFields = new ArrayList<>();
        for (String part : rawTargetField.split(",")) {
            String trimmed = part == null ? "" : part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            SqlInjectionValidator.validateFieldFormat(trimmed);
            assertRefFieldConfigured(refTable, trimmed, "refTargetField");
            targetFields.add(trimmed);
        }
        if (targetFields.isEmpty()) {
            throw new ServiceException("refTargetField 不能为空: field=" + col.getField());
        }

        // 字段级存在性校验（软约束：若关联表在 sys_column_meta 中已有配置，则强制命中）
        assertRefFieldConfigured(refTable, matchField, "refMatchField");

        // 别名 = column_meta.field 转 snake
        String aliasField = camelToSnake(col.getField());
        SqlInjectionValidator.validateFieldFormat(aliasField);

        String joinAlias = JOIN_ALIAS_PREFIX + aliasIdx;

        return new LookupColumn(
                joinAlias,
                refTable,
                localField,
                matchField,
                targetFields,
                aliasField,
                DEFAULT_DELETE_COLUMN,
                col.getField()
        );
    }

    /** 如果关联表已在 sys_column_meta 中配置过字段，则必须命中其中之一 */
    private void assertRefFieldConfigured(String refTable, String field, String label) {
        try {
            List<ColumnMeta> refCols = columnMetaMapper.selectByTableCode(refTable);
            if (refCols == null || refCols.isEmpty()) {
                // 降级：未配置字段元数据，仅依赖格式校验
                return;
            }
            for (ColumnMeta rc : refCols) {
                String rcSnake = camelToSnake(rc.getField());
                if (field.equalsIgnoreCase(rcSnake) || field.equalsIgnoreCase(rc.getField())) {
                    return;
                }
            }
            throw new ServiceException("关联字段不在 sys_column_meta 中: " + label + "=" + field + ", refTable=" + refTable);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.warn("assertRefFieldConfigured skipped due to error: {}", e.getMessage());
        }
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /** warehouseCode -> warehouse_code；已是 snake 则原样返回 */
    public static String camelToSnake(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.indexOf('_') >= 0) {
            return name.toLowerCase(Locale.ROOT);
        }
        return CAMEL_PATTERN.matcher(name).replaceAll("$1_$2").toLowerCase(Locale.ROOT);
    }

    /**
     * 承载搜索参数改写结果。
     * <ul>
     *   <li>values: key(虚拟列 snake field) → 搜索值</li>
     *   <li>sqlExpressions: key(虚拟列 snake field) → "j1.warehouse_name"</li>
     * </ul>
     */
    public static class VirtualParamHolder {
        private final Map<String, Object> values = new HashMap<>();
        private final Map<String, String> sqlExpressions = new HashMap<>();

        public Map<String, Object> getValues() { return values; }
        public Map<String, String> getSqlExpressions() { return sqlExpressions; }
        public boolean isEmpty() { return values.isEmpty(); }
    }
}
