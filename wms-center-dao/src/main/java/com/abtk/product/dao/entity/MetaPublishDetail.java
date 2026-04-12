package com.abtk.product.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 发布SQL执行明细
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetaPublishDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联发布记录ID */
    private Long publishId;

    /** 执行顺序 */
    private Integer seq;

    /** SQL类型：CREATE_TABLE/ALTER_ADD/ALTER_MODIFY/ALTER_DROP/CREATE_INDEX/DROP_INDEX */
    private String sqlType;

    /** SQL语句 */
    private String sqlText;

    /** 风险等级：SAFE/WARNING/DANGER */
    private String riskLevel;

    /** 执行耗时（毫秒） */
    private Integer executionTime = 0;

    /** 执行结果：PENDING/SUCCESS/FAILED/SKIPPED */
    private String resultStatus;

    /** 错误信息 */
    private String errorMessage;

    /** 执行时间 */
    private LocalDateTime executedAt;

    /** 非数据库字段 */
    private transient Boolean selected = true;

    // SQL类型常量
    public static final String SQL_TYPE_CREATE_TABLE = "CREATE_TABLE";
    public static final String SQL_TYPE_ALTER_ADD = "ALTER_ADD";
    public static final String SQL_TYPE_ALTER_MODIFY = "ALTER_MODIFY";
    public static final String SQL_TYPE_ALTER_DROP = "ALTER_DROP";
    public static final String SQL_TYPE_CREATE_INDEX = "CREATE_INDEX";
    public static final String SQL_TYPE_DROP_INDEX = "DROP_INDEX";

    // 风险等级常量
    public static final String RISK_SAFE = "SAFE";
    public static final String RISK_WARNING = "WARNING";
    public static final String RISK_DANGER = "DANGER";

    // 结果状态常量
    public static final String RESULT_PENDING = "PENDING";
    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_FAILED = "FAILED";
    public static final String RESULT_SKIPPED = "SKIPPED";
}
