package com.abtk.product.api.domain.response.sys;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 发布计划响应（预览）
 */
@Data
@Accessors(chain = true)
public class MetaPublishPlanResponse {

    /** 发布编码 */
    private String publishCode;

    /** 表编码 */
    private String tableCode;

    /** 表名称 */
    private String tableName;

    /** 版本号 */
    private Integer version;

    /** 校验结果 */
    private ValidationResult validation;

    /** Diff 结果 */
    private DiffResult diff;

    /** SQL 清单 */
    private List<SqlItem> sqlList;

    /** 整体风险等级 */
    private String overallRisk;

    /** 是否可以发布 */
    private Boolean canPublish;

    /** 不可发布原因 */
    private String reason;

    @Data
    public static class ValidationResult {
        private Boolean passed;
        private List<String> errors;
        private List<String> warnings;
    }

    @Data
    public static class DiffResult {
        /** 新增的字段 */
        private List<DiffColumn> addedColumns;
        /** 修改的字段 */
        private List<DiffColumn> modifiedColumns;
        /** 删除的字段 */
        private List<DiffColumn> removedColumns;
        /** 是否存在表 */
        private Boolean tableExists;
    }

    @Data
    public static class DiffColumn {
        private String columnCode;
        private String columnName;
        private String metaValue;
        private String dbValue;
        private String changeType;
    }

    @Data
    @Accessors(chain = true)
    public static class SqlItem {
        private Integer seq;
        private String sqlType;
        private String sqlText;
        private String riskLevel;
        private String riskReason;
        private Long detailId;
    }
}
