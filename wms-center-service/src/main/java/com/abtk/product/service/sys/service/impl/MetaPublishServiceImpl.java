package com.abtk.product.service.sys.service.impl;

import com.abtk.product.api.domain.request.sys.MetaPublishExecuteRequest;
import com.abtk.product.api.domain.request.sys.MetaPublishPlanRequest;
import com.abtk.product.api.domain.request.sys.MetaPublishQueryRequest;
import com.abtk.product.api.domain.response.sys.MetaPublishPlanResponse;
import com.abtk.product.api.domain.response.sys.MetaPublishResponse;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.dao.entity.*;
import com.abtk.product.dao.mapper.*;
import com.abtk.product.dao.util.SqlInjectionValidator;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.sys.cache.MetaCacheEvent;
import com.abtk.product.service.sys.service.MetaPublishService;
import com.abtk.product.service.system.service.I18nService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 元数据发布服务实现（V1：CREATE_TABLE / ADD_COLUMN / MODIFY_COLUMN / CREATE_INDEX）
 */
@Service
public class MetaPublishServiceImpl implements MetaPublishService {

    private static final Logger log = LoggerFactory.getLogger(MetaPublishServiceImpl.class);

    @Autowired
    private MetaPublishMapper publishMapper;

    @Autowired
    private MetaPublishDetailMapper detailMapper;

    @Autowired
    private MetaPublishSnapshotMapper snapshotMapper;

    @Autowired
    private TableMetaMapper tableMetaMapper;

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Autowired
    private TableOperationMapper operationMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private ObjectMapper objectMapper;

    // ==================== 发布计划 ====================

    @Override
    public MetaPublishPlanResponse generatePlan(MetaPublishPlanRequest request) {
        if (request.getTableCodes() == null || request.getTableCodes().isEmpty()) {
            throw new ServiceException("表编码列表不能为空");
        }
        return generatePlans(request).stream().findFirst().orElse(null);
    }

    @Override
    public List<MetaPublishPlanResponse> generatePlans(MetaPublishPlanRequest request) {
        List<MetaPublishPlanResponse> results = new ArrayList<>();
        for (String tableCode : request.getTableCodes()) {
            try {
                results.add(buildPlanForTable(tableCode, request.getForced()));
            } catch (Exception e) {
                log.error("生成发布计划失败 tableCode={}", tableCode, e);
                MetaPublishPlanResponse r = new MetaPublishPlanResponse();
                r.setTableCode(tableCode);
                r.setCanPublish(false);
                r.setReason("生成计划失败: " + e.getMessage());
                results.add(r);
            }
        }
        return results;
    }

    private MetaPublishPlanResponse buildPlanForTable(String tableCode, Boolean forced) {
        // 1. 校验表元数据
        TableMeta tableMeta = tableMetaMapper.selectByTableCode(tableCode);
        if (tableMeta == null) {
            throw new ServiceException("表元数据不存在: " + tableCode);
        }

        // 2. 校验字段元数据
        List<ColumnMeta> columns = columnMetaMapper.selectByTableCode(tableCode);
        if (columns == null || columns.isEmpty()) {
            throw new ServiceException("表 " + tableCode + " 没有配置任何字段，请先在字段元数据页面配置");
        }

        // 3. SQL 注入防护
        SqlInjectionValidator.validateTableFormat(tableCode);
        for (ColumnMeta col : columns) {
            SqlInjectionValidator.validateFieldFormat(col.getField());
        }

        // 4. 生成版本号
        Long maxVersion = publishMapper.selectMaxVersionByTableCode(tableCode);
        int nextVersion = (maxVersion == null ? 0 : maxVersion.intValue()) + 1;

        // 5. 元数据校验
        MetaPublishPlanResponse.ValidationResult validation = validateMetadata(tableMeta, columns);

        // 6. Diff + SQL 生成
        MetaPublishPlanResponse.DiffResult diff = diffTable(tableCode, columns);
        List<MetaPublishPlanResponse.SqlItem> sqlItems = generateSqlList(tableCode, tableMeta, columns, diff, forced);

        log.info("生成SQL清单: tableCode={}, diff.tableExists={}, added={}, modified={}, removed={}, sqlItems.size={}",
                tableCode, diff.getTableExists(),
                diff.getAddedColumns() != null ? diff.getAddedColumns().size() : 0,
                diff.getModifiedColumns() != null ? diff.getModifiedColumns().size() : 0,
                diff.getRemovedColumns() != null ? diff.getRemovedColumns().size() : 0,
                sqlItems.size());
        for (MetaPublishPlanResponse.SqlItem item : sqlItems) {
            log.info("  SQL: seq={}, type={}, text={}", item.getSeq(), item.getSqlType(), item.getSqlText());
        }

        // 7. 整体风险评估
        String overallRisk = assessOverallRisk(sqlItems);
        boolean canPublish = validation.getPassed() && !sqlItems.isEmpty()
                && (forced || !overallRisk.equals(MetaPublishDetail.RISK_DANGER));

        MetaPublishPlanResponse response = new MetaPublishPlanResponse();
        response.setTableCode(tableCode);
        response.setTableName(tableMeta.getTableName());
        response.setVersion(nextVersion);
        response.setValidation(validation);
        response.setDiff(diff);
        response.setSqlList(sqlItems);
        response.setOverallRisk(overallRisk);
        response.setCanPublish(canPublish);
        response.setReason(canPublish ? null : buildCannotPublishReason(validation, sqlItems, overallRisk, forced));

        return response;
    }

    private MetaPublishPlanResponse.ValidationResult validateMetadata(TableMeta tableMeta, List<ColumnMeta> columns) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // 主键校验：最多只能有1个，若无则系统自动兜底（id自增主键）
        long pkCount = columns.stream().filter(c -> Boolean.TRUE.equals(c.getPrimaryKey())).count();
        if (pkCount > 1) {
            errors.add("主键字段不能超过1个，当前有 " + pkCount + " 个");
        }
        // pkCount == 0 时不报错，系统会在生成 DDL 时自动兜底 id 自增主键

        // 字段重名校验
        Set<String> fieldNames = new HashSet<>();
        for (ColumnMeta col : columns) {
            if (fieldNames.contains(col.getField())) {
                errors.add("字段编码重复: " + col.getField());
            }
            fieldNames.add(col.getField());
        }

        // 数据类型校验
        Set<String> validTypes = new HashSet<>(Arrays.asList(
                "string", "text", "int", "bigint", "decimal", "date", "datetime", "boolean", "double", "float"
        ));
        for (ColumnMeta col : columns) {
            if (col.getDataType() == null || StringUtils.isEmpty(col.getDataType())) {
                errors.add("字段 " + col.getField() + " 数据类型不能为空");
            } else if (!validTypes.contains(col.getDataType().toLowerCase())) {
                errors.add("字段 " + col.getField() + " 数据类型不合法: " + col.getDataType());
            }
        }


        MetaPublishPlanResponse.ValidationResult result = new MetaPublishPlanResponse.ValidationResult();
        result.setPassed(errors.isEmpty());
        result.setErrors(errors);
        result.setWarnings(warnings);
        return result;
    }

    private MetaPublishPlanResponse.DiffResult diffTable(String tableCode, List<ColumnMeta> metaColumns) {
        MetaPublishPlanResponse.DiffResult diff = new MetaPublishPlanResponse.DiffResult();

        // 查询真实数据库表结构
        List<Map<String, Object>> dbColumns = queryDbColumns(tableCode);
        boolean tableExists = dbColumns != null && !dbColumns.isEmpty();
        diff.setTableExists(tableExists);

        log.info("diffTable: tableCode={}, tableExists={}, dbColumns.size={}, metaColumns.size={}",
                tableCode, tableExists, dbColumns != null ? dbColumns.size() : 0, metaColumns.size());

        Map<String, Map<String, Object>> dbColMap = new HashMap<>();
        if (dbColumns != null) {
            for (Map<String, Object> row : dbColumns) {
                String colName = String.valueOf(row.get("column_name"));
                dbColMap.put(colName.toLowerCase(), row);
            }
        }

        List<MetaPublishPlanResponse.DiffColumn> added = new ArrayList<>();
        List<MetaPublishPlanResponse.DiffColumn> modified = new ArrayList<>();
        List<MetaPublishPlanResponse.DiffColumn> removed = new ArrayList<>();

        Set<String> processedDbCols = new HashSet<>();

        for (ColumnMeta mc : metaColumns) {
            String field = mc.getField();
            Map<String, Object> dbCol = dbColMap.get(field.toLowerCase());

            if (dbCol == null) {
                // 新增
                MetaPublishPlanResponse.DiffColumn dc = new MetaPublishPlanResponse.DiffColumn();
                dc.setColumnCode(field);
                dc.setColumnName(mc.getTitle());
                dc.setMetaValue(formatMetaColumnValue(mc));
                dc.setDbValue(null);
                dc.setChangeType("ADD");
                added.add(dc);
            } else {
                processedDbCols.add(field.toLowerCase());
                // 检查是否修改
                String metaType = mapDataTypeToDbType(mc.getDataType(), mc.getColumnSize(), mc.getDecimalDigits());
                String dbType = String.valueOf(dbCol.get("column_type"));
                if (!normalizeDbType(metaType).equals(normalizeDbType(dbType))) {
                    MetaPublishPlanResponse.DiffColumn dc = new MetaPublishPlanResponse.DiffColumn();
                    dc.setColumnCode(field);
                    dc.setColumnName(mc.getTitle());
                    dc.setMetaValue(metaType);
                    dc.setDbValue(dbType);
                    dc.setChangeType("MODIFY");
                    modified.add(dc);
                }
            }
        }

        if (tableExists) {
            for (String dbColName : dbColMap.keySet()) {
                if (!processedDbCols.contains(dbColName)) {
                    Map<String, Object> dbCol = dbColMap.get(dbColName);
                    MetaPublishPlanResponse.DiffColumn dc = new MetaPublishPlanResponse.DiffColumn();
                    dc.setColumnCode(dbColName);
                    dc.setColumnName(String.valueOf(dbCol.get("column_name")));
                    dc.setMetaValue(null);
                    dc.setDbValue(String.valueOf(dbCol.get("column_type")));
                    dc.setChangeType("REMOVE");
                    removed.add(dc);
                }
            }
        }

        diff.setAddedColumns(added);
        diff.setModifiedColumns(modified);
        diff.setRemovedColumns(removed);
        return diff;
    }

    private List<Map<String, Object>> queryDbColumns(String tableCode) {
        try {
            // 先检查表是否存在
            String checkSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, tableCode);
            if (count == null || count == 0) {
                return Collections.emptyList();
            }
            return jdbcTemplate.queryForList(
                    "SELECT column_name, column_type, column_key, is_nullable, character_maximum_length, numeric_precision, numeric_scale "
                            + "FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = ? ORDER BY ordinal_position",
                    tableCode
            );
        } catch (Exception e) {
            log.warn("查询表结构失败 tableCode={}: {}", tableCode, e.getMessage());
            return Collections.emptyList();
        }
    }

    private boolean tableExists(String tableCode) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableCode);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private List<MetaPublishPlanResponse.SqlItem> generateSqlList(String tableCode, TableMeta tableMeta,
                                                                   List<ColumnMeta> columns,
                                                                   MetaPublishPlanResponse.DiffResult diff,
                                                                   Boolean forced) {
        List<MetaPublishPlanResponse.SqlItem> items = new ArrayList<>();
        int seq = 1;

        log.info("generateSqlList: tableCode={}, tableExists={}, diff.added={}, diff.modified={}, diff.removed={}",
                tableCode, diff.getTableExists(),
                diff.getAddedColumns() != null ? diff.getAddedColumns().size() : 0,
                diff.getModifiedColumns() != null ? diff.getModifiedColumns().size() : 0,
                diff.getRemovedColumns() != null ? diff.getRemovedColumns().size() : 0);

        // 如果表不存在，生成 CREATE TABLE
        if (!Boolean.TRUE.equals(diff.getTableExists())) {
            String createTableSql = generateCreateTableSql(tableCode, tableMeta, columns);
            String riskLevel = assessCreateTableRisk(createTableSql);
            items.add(buildSqlItem(seq++, MetaPublishDetail.SQL_TYPE_CREATE_TABLE, createTableSql, riskLevel,
                    "新建表 " + tableCode));
        }

        // 生成 ADD COLUMN
        if (diff.getAddedColumns() != null) {
            for (MetaPublishPlanResponse.DiffColumn dc : diff.getAddedColumns()) {
                ColumnMeta col = columns.stream().filter(c -> c.getField().equalsIgnoreCase(dc.getColumnCode())).findFirst().orElse(null);
                if (col != null) {
                    String alterSql = generateAddColumnSql(tableCode, col);
                    items.add(buildSqlItem(seq++, MetaPublishDetail.SQL_TYPE_ALTER_ADD, alterSql,
                            MetaPublishDetail.RISK_SAFE, "新增字段 " + dc.getColumnCode()));
                }
            }
        }

        // 生成 MODIFY COLUMN（V1.1）
        if (diff.getModifiedColumns() != null && !diff.getModifiedColumns().isEmpty()) {
            for (MetaPublishPlanResponse.DiffColumn dc : diff.getModifiedColumns()) {
                ColumnMeta col = columns.stream().filter(c -> c.getField().equalsIgnoreCase(dc.getColumnCode())).findFirst().orElse(null);
                if (col != null) {
                    String alterSql = generateModifyColumnSql(tableCode, col);
                    String riskLevel = assessModifyColumnRisk(dc.getDbValue(), formatMetaColumnValue(col));
                    items.add(buildSqlItem(seq++, MetaPublishDetail.SQL_TYPE_ALTER_MODIFY, alterSql, riskLevel,
                            "修改字段 " + dc.getColumnCode() + "：" + dc.getDbValue() + " → " + dc.getMetaValue()));
                }
            }
        }

        // 生成索引（仅当表存在时才建索引）
        if (Boolean.TRUE.equals(diff.getTableExists())) {
            List<String> indexSqls = generateIndexSqls(tableCode, columns);
            for (String idxSql : indexSqls) {
                items.add(buildSqlItem(seq++, MetaPublishDetail.SQL_TYPE_CREATE_INDEX, idxSql,
                        MetaPublishDetail.RISK_SAFE, "创建索引"));
            }
        }

        return items;
    }

    private String generateCreateTableSql(String tableCode, TableMeta tableMeta, List<ColumnMeta> columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `").append(tableCode).append("` (\n");

        List<String> columnDefs = new ArrayList<>();

        // 若没有主键字段，自动兜底 id 自增主键
        boolean hasPk = columns.stream().anyMatch(c -> Boolean.TRUE.equals(c.getPrimaryKey()));
        if (!hasPk) {
            columnDefs.add("  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID'");
        }

        for (ColumnMeta col : columns) {
            columnDefs.add(generateColumnDefinition(col, true));
        }

        String delCol = "is_deleted";
        columnDefs.add("  `" + delCol + "` CHAR(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记'");


        sb.append(String.join(",\n", columnDefs));

        // 主键约束
        if (!hasPk) {
            sb.append(",\n  PRIMARY KEY (`id`)");
        } else {
            List<String> pkFields = columns.stream()
                    .filter(c -> Boolean.TRUE.equals(c.getPrimaryKey()))
                    .map(c -> "`" + c.getField() + "`")
                    .collect(Collectors.toList());
            sb.append(",\n  PRIMARY KEY (").append(String.join(", ", pkFields)).append(")");
        }

        sb.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='").append(escapeComment(tableMeta.getTableName())).append("'");
        return sb.toString();
    }

    private String generateColumnDefinition(ColumnMeta col, boolean includeComment) {
        String field = col.getField();
        String dbType = mapDataTypeToDbType(col.getDataType(), col.getColumnSize(), col.getDecimalDigits());
        StringBuilder sb = new StringBuilder();
        sb.append("  `").append(field).append("` ").append(dbType);

        if (Boolean.TRUE.equals(col.getPrimaryKey())) {
            sb.append(" NOT NULL");
        } else if (Boolean.FALSE.equals(col.getNullable())) {
            sb.append(" NOT NULL");
        } else {
            sb.append(" NULL");
        }

        if (col.getDefaultValue() != null && !StringUtils.isEmpty(col.getDefaultValue())) {
            sb.append(" DEFAULT ").append(col.getDefaultValue());
        }

        if (includeComment && col.getTitle() != null && !StringUtils.isEmpty(col.getTitle())) {
            sb.append(" COMMENT '").append(escapeComment(col.getTitle())).append("'");
        }

        return sb.toString();
    }

    private String generateAddColumnSql(String tableCode, ColumnMeta col) {
        String def = generateColumnDefinition(col, true);
        return "ALTER TABLE `" + tableCode + "` ADD COLUMN " + def.substring(2);
    }

    private String generateModifyColumnSql(String tableCode, ColumnMeta col) {
        String field = col.getField();
        String dbType = mapDataTypeToDbType(col.getDataType(), col.getColumnSize(), col.getDecimalDigits());
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE `").append(tableCode).append("` MODIFY COLUMN `").append(field).append("` ").append(dbType);

        if (Boolean.TRUE.equals(col.getPrimaryKey())) {
            sb.append(" NOT NULL");
        } else if (Boolean.FALSE.equals(col.getNullable())) {
            sb.append(" NOT NULL");
        } else {
            sb.append(" NULL");
        }

        if (col.getDefaultValue() != null && !StringUtils.isEmpty(col.getDefaultValue())) {
            sb.append(" DEFAULT ").append(col.getDefaultValue());
        }

        if (col.getTitle() != null && !StringUtils.isEmpty(col.getTitle())) {
            sb.append(" COMMENT '").append(escapeComment(col.getTitle())).append("'");
        }

        return sb.toString();
    }

    private List<String> generateIndexSqls(String tableCode, List<ColumnMeta> columns) {
        return Collections.emptyList();
    }

    private String mapDataTypeToDbType(String dataType, Integer columnSize, Integer decimalDigits) {
        if (dataType == null) return "VARCHAR(255)";
        switch (dataType.toLowerCase()) {
            case "string":
            case "text":
                return columnSize != null && columnSize > 0 ? "VARCHAR(" + columnSize + ")" : "VARCHAR(255)";
            case "int":
                return "INT";
            case "bigint":
                return "BIGINT";
            case "decimal":
                if (decimalDigits != null && decimalDigits > 0) {
                    int intDigits = (columnSize != null ? columnSize : 10) - decimalDigits;
                    return "DECIMAL(" + columnSize + "," + decimalDigits + ")";
                }
                return "DECIMAL(10,2)";
            case "date":
                return "DATE";
            case "datetime":
                return "DATETIME";
            case "boolean":
                return "TINYINT(1)";
            case "double":
                return "DOUBLE";
            case "float":
                return "FLOAT";
            default:
                return "VARCHAR(255)";
        }
    }

    private String formatMetaColumnValue(ColumnMeta col) {
        return mapDataTypeToDbType(col.getDataType(), col.getColumnSize(), col.getDecimalDigits());
    }

    private String normalizeDbType(String dbType) {
        if (dbType == null) return "";
        return dbType.toUpperCase().replaceAll("\\s+", " ").trim();
    }

    private String escapeComment(String comment) {
        if (comment == null) return "";
        return comment.replace("'", "''");
    }

    private String assessCreateTableRisk(String sql) {
        return MetaPublishDetail.RISK_SAFE;
    }

    private String assessModifyColumnRisk(String oldType, String newType) {
        // 缩短长度 或 变窄类型 = DANGER
        int oldLen = extractLength(oldType);
        int newLen = extractLength(newType);
        if (oldLen > 0 && newLen > 0 && newLen < oldLen) {
            return MetaPublishDetail.RISK_DANGER;
        }
        return MetaPublishDetail.RISK_WARNING;
    }

    private int extractLength(String dbType) {
        if (dbType == null) return 0;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\(([^)]+)\\)").matcher(dbType);
        if (m.find()) {
            String[] parts = m.group(1).split(",");
            try {
                return Integer.parseInt(parts[0].trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private String assessOverallRisk(List<MetaPublishPlanResponse.SqlItem> items) {
        if (items == null || items.isEmpty()) return MetaPublishDetail.RISK_SAFE;
        boolean hasWarning = false;
        for (MetaPublishPlanResponse.SqlItem item : items) {
            if (MetaPublishDetail.RISK_DANGER.equals(item.getRiskLevel())) {
                return MetaPublishDetail.RISK_DANGER;
            }
            if (MetaPublishDetail.RISK_WARNING.equals(item.getRiskLevel())) {
                hasWarning = true;
            }
        }
        return hasWarning ? MetaPublishDetail.RISK_WARNING : MetaPublishDetail.RISK_SAFE;
    }

    private String buildCannotPublishReason(MetaPublishPlanResponse.ValidationResult validation,
                                             List<MetaPublishPlanResponse.SqlItem> items,
                                             String overallRisk, Boolean forced) {
        if (!validation.getPassed()) {
            return "校验未通过: " + String.join("; ", validation.getErrors());
        }
        if (items.isEmpty()) {
            return "没有需要执行的 SQL";
        }
        if (MetaPublishDetail.RISK_DANGER.equals(overallRisk) && !Boolean.TRUE.equals(forced)) {
            return "存在高风险 SQL（数据截断风险），请确认是否强制执行";
        }
        return "无法发布";
    }

    private MetaPublishPlanResponse.SqlItem buildSqlItem(int seq, String sqlType, String sqlText,
                                                          String riskLevel, String riskReason) {
        MetaPublishPlanResponse.SqlItem item = new MetaPublishPlanResponse.SqlItem();
        item.setSeq(seq);
        item.setSqlType(sqlType);
        item.setSqlText(sqlText);
        item.setRiskLevel(riskLevel);
        item.setRiskReason(riskReason);
        return item;
    }

    // ==================== 发布计划保存 ====================

    /**
     * 生成并保存发布计划（内部方法，供 execute 前的预览使用）
     */
    public MetaPublish savePlan(MetaPublishPlanRequest request) {
        if (request.getTableCodes() == null || request.getTableCodes().isEmpty()) {
            throw new ServiceException("表编码列表不能为空");
        }
        String tableCode = request.getTableCodes().get(0);
        MetaPublishPlanResponse plan = buildPlanForTable(tableCode, request.getForced());

        // 生成发布编码
        String publishCode = UUID.randomUUID().toString();

        // 查找表元数据
        TableMeta tableMeta = tableMetaMapper.selectByTableCode(tableCode);
        Long maxVersion = publishMapper.selectMaxVersionByTableCode(tableCode);
        int nextVersion = (maxVersion == null ? 0 : maxVersion.intValue()) + 1;

        // 保存发布主记录
        MetaPublish publish = new MetaPublish();
        publish.setPublishCode(publishCode);
        publish.setTableCode(tableCode);
        publish.setTableName(tableMeta != null ? tableMeta.getTableName() : tableCode);
        publish.setVersion(nextVersion);
        publish.setStatus(MetaPublish.STATUS_PENDING);
        publish.setForced(Boolean.TRUE.equals(request.getForced()));
        publish.setRemark(request.getRemark());
        publish.setPublishBy(SecurityUtils.getUsername());
        publishMapper.insert(publish);

        // 保存明细
        List<MetaPublishDetail> details = new ArrayList<>();
        int seq = 1;
        for (MetaPublishPlanResponse.SqlItem item : plan.getSqlList()) {
            MetaPublishDetail detail = new MetaPublishDetail();
            detail.setPublishId(publish.getId());
            detail.setSeq(seq++);
            detail.setSqlType(item.getSqlType());
            detail.setSqlText(item.getSqlText());
            detail.setRiskLevel(item.getRiskLevel());
            detail.setResultStatus(MetaPublishDetail.RESULT_PENDING);
            details.add(detail);
        }
        publish.setTotalSqls(details.size());
        publishMapper.updateById(publish);

        if (!details.isEmpty()) {
            detailMapper.batchInsert(details);
        }

        // 保存快照
        try {
            MetaPublishSnapshot snapshot = new MetaPublishSnapshot();
            snapshot.setPublishId(publish.getId());
            snapshot.setTableCode(tableCode);
            snapshot.setSnapshotType(MetaPublishSnapshot.TYPE_BEFORE);
            TableMeta tm = tableMetaMapper.selectByTableCode(tableCode);
            if (tm != null) {
                snapshot.setTableMetaJson(objectMapper.writeValueAsString(tm));
            }
            List<ColumnMeta> cols = columnMetaMapper.selectByTableCode(tableCode);
            if (cols != null && !cols.isEmpty()) {
                snapshot.setColumnMetaJson(objectMapper.writeValueAsString(cols));
            }
            List<TableOperation> ops = operationMapper.selectByTableCode(tableCode);
            if (ops != null && !ops.isEmpty()) {
                snapshot.setOperationMetaJson(objectMapper.writeValueAsString(ops));
            }
            snapshotMapper.insert(snapshot);
        } catch (Exception e) {
            log.warn("保存快照失败: {}", e.getMessage());
        }

        return publish;
    }

    // ==================== 执行发布 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MetaPublishResponse execute(MetaPublishExecuteRequest request) {
        String publishCode = request.getPublishCode();

        // 查询待执行的发布记录
        MetaPublish publish = publishMapper.selectByPublishCode(publishCode);
        if (publish == null) {
            throw new ServiceException("发布记录不存在: " + publishCode);
        }
        if (!MetaPublish.STATUS_PENDING.equals(publish.getStatus())) {
            throw new ServiceException("发布记录状态不是待执行状态: " + publish.getStatus());
        }

        // 更新状态为执行中
        publish.setStatus(MetaPublish.STATUS_RUNNING);
        publishMapper.updateById(publish);

        // 发布前快照（已保存过，仅更新快照时间戳）
        // 加载明细
        List<MetaPublishDetail> details = detailMapper.selectByPublishId(publish.getId());
        if (details == null) {
            details = new ArrayList<>();
        }

        // 执行 SQL
        int successCount = 0;
        int failCount = 0;
        StringBuilder errorMsg = new StringBuilder();

        for (MetaPublishDetail detail : details) {
            if (request.getDetailIds() != null && !request.getDetailIds().isEmpty()
                    && !request.getDetailIds().contains(detail.getId())) {
                detail.setResultStatus(MetaPublishDetail.RESULT_SKIPPED);
                detailMapper.updateById(detail);
                continue;
            }

            long start = System.currentTimeMillis();
            try {
                jdbcTemplate.execute(detail.getSqlText());
                long elapsed = System.currentTimeMillis() - start;
                detail.setExecutionTime((int) elapsed);
                detail.setResultStatus(MetaPublishDetail.RESULT_SUCCESS);
                detailMapper.updateById(detail);
                successCount++;
                log.info("SQL执行成功: {}ms, {}", elapsed, detail.getSqlText().substring(0, Math.min(100, detail.getSqlText().length())));
            } catch (Exception e) {
                long elapsed = System.currentTimeMillis() - start;
                detail.setExecutionTime((int) elapsed);
                detail.setResultStatus(MetaPublishDetail.RESULT_FAILED);
                detail.setErrorMessage(e.getMessage());
                detailMapper.updateById(detail);
                failCount++;
                errorMsg.append("[").append(detail.getSeq()).append("] ").append(e.getMessage()).append("; ");
                log.error("SQL执行失败: {}", detail.getSqlText(), e);
            }
        }

        // 更新发布状态
        String finalStatus = failCount == 0 ? MetaPublish.STATUS_SUCCESS : MetaPublish.STATUS_FAILED;
        publish.setStatus(finalStatus);
        publish.setSuccessSqls(successCount);
        publish.setFailedSqls(failCount);
        publish.setErrorMessage(errorMsg.length() > 0 ? errorMsg.toString() : null);
        publish.setPublishTime(LocalDateTime.now());
        publishMapper.updateById(publish);

        // 刷新元数据缓存
        eventPublisher.publishEvent(new MetaCacheEvent(this, publish.getTableCode(), MetaCacheEvent.ChangeType.FULL_REFRESH));

        return toResponse(publish, details);
    }

    // ==================== 查询发布历史 ====================

    @Override
    public List<MetaPublishResponse> queryHistory(MetaPublishQueryRequest request) {
        List<MetaPublish> records = publishMapper.selectPage(
                request.getTableCode(), request.getStatus(),
                request.getPublishBy(), request.getBeginTime(), request.getEndTime()
        );
        return records.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public MetaPublishResponse getById(Long id) {
        MetaPublish publish = publishMapper.selectById(id);
        if (publish == null) {
            throw new ServiceException("发布记录不存在: " + id);
        }
        List<MetaPublishDetail> details = detailMapper.selectByPublishId(id);
        return toResponse(publish, details);
    }

    @Override
    public MetaPublishResponse getByCode(String publishCode) {
        MetaPublish publish = publishMapper.selectByPublishCode(publishCode);
        if (publish == null) {
            throw new ServiceException("发布记录不存在: " + publishCode);
        }
        List<MetaPublishDetail> details = detailMapper.selectByPublishId(publish.getId());
        return toResponse(publish, details);
    }

    // ==================== 回滚 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(Long publishId) {
        // V2 实现：按快照回滚
        throw new ServiceException("回滚功能 V2 实现");
    }

    // ==================== 辅助方法 ====================

    private MetaPublishResponse toResponse(MetaPublish publish, List<MetaPublishDetail> details) {
        MetaPublishResponse response = new MetaPublishResponse();
        response.setId(publish.getId());
        response.setPublishCode(publish.getPublishCode());
        response.setTableCode(publish.getTableCode());
        response.setTableName(publish.getTableName());
        response.setVersion(publish.getVersion());
        response.setStatus(publish.getStatus());
        response.setTotalSqls(publish.getTotalSqls());
        response.setSuccessSqls(publish.getSuccessSqls());
        response.setFailedSqls(publish.getFailedSqls());
        response.setErrorMessage(publish.getErrorMessage());
        response.setForced(publish.getForced());
        response.setPublishBy(publish.getPublishBy());
        response.setPublishByName(publish.getPublishByName());
        response.setPublishTime(publish.getPublishTime());
        response.setRemark(publish.getRemark());
        response.setCreatedAt(publish.getCreatedAt());

        if (details != null) {
            List<MetaPublishResponse.MetaPublishDetailResponse> detailResponses = details.stream().map(d -> {
                MetaPublishResponse.MetaPublishDetailResponse dr = new MetaPublishResponse.MetaPublishDetailResponse();
                dr.setId(d.getId());
                dr.setSeq(d.getSeq());
                dr.setSqlType(d.getSqlType());
                dr.setSqlText(d.getSqlText());
                dr.setRiskLevel(d.getRiskLevel());
                dr.setExecutionTime(d.getExecutionTime());
                dr.setResultStatus(d.getResultStatus());
                dr.setErrorMessage(d.getErrorMessage());
                dr.setExecutedAt(d.getExecutedAt());
                return dr;
            }).collect(Collectors.toList());
            response.setDetails(detailResponses);
        }

        return response;
    }

    private MetaPublishResponse toResponse(MetaPublish publish) {
        return toResponse(publish, null);
    }
}
