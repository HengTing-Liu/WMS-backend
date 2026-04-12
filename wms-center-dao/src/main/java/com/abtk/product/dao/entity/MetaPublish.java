package com.abtk.product.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 低代码元数据发布主记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetaPublish extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 发布编码（UUID） */
    private String publishCode;

    /** 目标表编码 */
    private String tableCode;

    /** 目标表名称 */
    private String tableName;

    /** 发布版本号 */
    private Integer version = 1;

    /** 发布状态：PENDING/RUNNING/SUCCESS/FAILED/ROLLED_BACK */
    private String status;

    /** SQL总数 */
    private Integer totalSqls = 0;

    /** 成功SQL数 */
    private Integer successSqls = 0;

    /** 失败SQL数 */
    private Integer failedSqls = 0;

    /** 失败错误信息 */
    private String errorMessage;

    /** 发布前快照JSON */
    private String snapshotData;

    /** 是否强制执行 */
    private Boolean forced = false;

    /** 发布人 */
    private String publishBy;

    /** 发布人姓名 */
    private String publishByName;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 非数据库字段 */
    private transient java.util.List<MetaPublishDetail> details;

    // 状态常量
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_ROLLED_BACK = "ROLLED_BACK";
}
