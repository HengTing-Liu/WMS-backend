package com.abtk.product.dao.support.lookup;

import java.util.Collections;
import java.util.List;

/**
 * Lookup 虚拟列预校验后的描述符。
 *
 * <p>该对象的所有字段都已通过 {@link com.abtk.product.dao.util.SqlInjectionValidator}
 * 格式校验与白名单校验，可直接作为 MyBatis {@code ${}} 占位符使用。</p>
 *
 * <p>实现策略（WMS-LOWCODE-LOOKUP-DEDUP）：<b>相关子查询 + LIMIT 1</b>，避免
 * 关联表存在多条匹配记录时把主表行复制成多行。{@link #joinAlias} 字段仅保留用于
 * 历史调试，运行时不再产生 LEFT JOIN。</p>
 *
 * <p>WMS-LOWCODE-LOOKUP-SEP：多字段拼接分隔符可自定义，由 service 层
 * 通过 {@code separatorParams} Map 参数注入，运行时以 {@code #{separatorParams.sep_xxx}}
 * 参数化传递，不产生 SQL 注入面。</p>
 *
 * <p>典型 SQL 结构：</p>
 * <pre>
 * -- 单字段
 * SELECT m.*,
 *        (SELECT j.warehouse_name FROM sys_warehouse j
 *          WHERE j.warehouse_code = m.warehouse_code
 *            AND j.is_deleted = 0
 *          LIMIT 1) AS warehouse_name
 * FROM inv_location m
 * WHERE m.is_deleted = 0
 *
 * -- 多字段拼接（WMS-LOWCODE-LOOKUP-CONCAT + SEP：分隔符参数化）
 * SELECT m.*,
 *        (SELECT CONCAT_WS(?, j.warehouse_code, j.warehouse_name)
 *           FROM sys_warehouse j
 *          WHERE j.warehouse_code = m.warehouse_code
 *            AND j.is_deleted = 0
 *          LIMIT 1) AS warehouse_label
 * FROM inv_location m
 * WHERE m.is_deleted = 0
 * </pre>
 *
 * <p>放在 dao 模块，供 DynamicMapper 作为参数类型引用。</p>
 */
public class LookupColumn {

    /** 多字段拼接默认分隔符（WMS-LOWCODE-LOOKUP-CONCAT，保留为兜底 / 注释语义）。 */
    public static final String CONCAT_SEPARATOR = "\u2764";

    /** separatorParams 的 key 前缀（WMS-LOWCODE-LOOKUP-SEP） */
    public static final String SEPARATOR_PARAM_PREFIX = "sep_";

    /** JOIN 别名（j1 / j2 / ...） */
    private final String joinAlias;

    /** 关联表 tableCode（snake_case） */
    private final String refTable;

    /** 当前表外键列（snake_case） */
    private final String localField;

    /** 关联表匹配列（snake_case） */
    private final String matchField;

    /**
     * 关联表展示列（snake_case）列表。
     * <ul>
     *   <li>单字段：size == 1</li>
     *   <li>多字段拼接：size &gt; 1，按顺序拼接，分隔符由 {@link #separator} 指定</li>
     * </ul>
     * 每个元素都已通过 {@code SAFE_FIELD_PATTERN} 校验。
     */
    private final List<String> targetFields;

    /** SELECT 出来的别名（= column_meta.field 的 snake_case 形式） */
    private final String aliasField;

    /** 关联表逻辑删除列（当前统一为 is_deleted） */
    private final String deleteColumn;

    /** 原始 column_meta.field（驼峰，供前端匹配） */
    private final String virtualFieldCamel;

    /**
     * 多字段拼接分隔符（WMS-LOWCODE-LOOKUP-SEP）。
     * <p>仅在 {@link #isConcat()} == true 时生效；null/空时由 {@code LookupSqlBuilder} 兜底为 {@link #CONCAT_SEPARATOR}。
     * 该值通过 MyBatis {@code #{}} 参数化注入，不直接拼入 SQL。</p>
     */
    private final String separator;

    public LookupColumn(String joinAlias,
                        String refTable,
                        String localField,
                        String matchField,
                        List<String> targetFields,
                        String aliasField,
                        String deleteColumn,
                        String virtualFieldCamel) {
        this(joinAlias, refTable, localField, matchField, targetFields,
                aliasField, deleteColumn, virtualFieldCamel, CONCAT_SEPARATOR);
    }

    public LookupColumn(String joinAlias,
                        String refTable,
                        String localField,
                        String matchField,
                        List<String> targetFields,
                        String aliasField,
                        String deleteColumn,
                        String virtualFieldCamel,
                        String separator) {
        this.joinAlias = joinAlias;
        this.refTable = refTable;
        this.localField = localField;
        this.matchField = matchField;
        this.targetFields = targetFields == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(targetFields);
        this.aliasField = aliasField;
        this.deleteColumn = deleteColumn;
        this.virtualFieldCamel = virtualFieldCamel;
        this.separator = (separator == null || separator.isEmpty()) ? CONCAT_SEPARATOR : separator;
    }

    public String getJoinAlias() { return joinAlias; }
    public String getRefTable() { return refTable; }
    public String getLocalField() { return localField; }
    public String getMatchField() { return matchField; }
    public List<String> getTargetFields() { return targetFields; }
    public String getAliasField() { return aliasField; }
    public String getDeleteColumn() { return deleteColumn; }
    public String getVirtualFieldCamel() { return virtualFieldCamel; }
    public String getSeparator() { return separator; }

    /**
     * separatorParams Map 的 key（WMS-LOWCODE-LOOKUP-SEP）。
     * service 层用 {@code Map.put(getSeparatorParamKey(), getSeparator())} 构造参数 Map。
     * XML 中通过 {@code #{separatorParams.sep_xxx}} 引用。
     */
    public String getSeparatorParamKey() {
        return SEPARATOR_PARAM_PREFIX + aliasField;
    }

    /**
     * 兼容性 getter：返回逗号拼接的原始 targetField 表示（仅供日志/调试，不用于 SQL 拼接）。
     */
    public String getTargetField() {
        if (targetFields == null || targetFields.isEmpty()) {
            return "";
        }
        if (targetFields.size() == 1) {
            return targetFields.get(0);
        }
        return String.join(",", targetFields);
    }

    /**
     * 是否为多字段拼接列。
     */
    public boolean isConcat() {
        return targetFields != null && targetFields.size() > 1;
    }

    /**
     * SQL 表达式（供 SELECT 列、WHERE、ORDER BY 使用）。
     *
     * <p>WMS-LOWCODE-LOOKUP-DEDUP：采用<b>相关子查询 + LIMIT 1</b>，保证主表行数不受
     * 关联表"一对多"数据影响。</p>
     *
     * <ul>
     *   <li>单字段：{@code (SELECT j.warehouse_name FROM sys_warehouse j WHERE j.warehouse_code = m.warehouse_code AND j.is_deleted = 0 LIMIT 1)}</li>
     *   <li>多字段拼接：{@code (SELECT CONCAT_WS('❤', j.warehouse_code, j.warehouse_name) FROM sys_warehouse j WHERE ... LIMIT 1)}</li>
     * </ul>
     *
     * <p>所有字段都已通过白名单 + 格式校验，拼接结果可安全用作 MyBatis {@code ${}} 占位符。</p>
     */
    public String getSqlExpression() {
        StringBuilder sb = new StringBuilder("(SELECT ");
        if (targetFields == null || targetFields.isEmpty()) {
            sb.append("NULL");
        } else if (targetFields.size() == 1) {
            sb.append("j.").append(targetFields.get(0));
        } else {
            // WMS-LOWCODE-LOOKUP-SEP：分隔符走 MyBatis 参数化（${} 展开后 #{} 仍被 SqlSourceBuilder 解析为 ?）
            sb.append("CONCAT_WS(#{separatorParams.")
              .append(getSeparatorParamKey())
              .append("}");
            for (String f : targetFields) {
                sb.append(", j.").append(f);
            }
            sb.append(")");
        }
        sb.append(" FROM ").append(refTable).append(" j")
          .append(" WHERE j.").append(matchField).append(" = m.").append(localField)
          .append(" AND j.").append(deleteColumn).append(" = 0")
          .append(" LIMIT 1)");
        return sb.toString();
    }
}
