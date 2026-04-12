package com.abtk.product.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 发布快照（支持回滚）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetaPublishSnapshot extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联发布记录ID */
    private Long publishId;

    /** 表编码 */
    private String tableCode;

    /** 快照类型：BEFORE/AFTER */
    private String snapshotType;

    /** 表元数据JSON */
    private String tableMetaJson;

    /** 字段元数据JSON */
    private String columnMetaJson;

    /** 操作按钮元数据JSON */
    private String operationMetaJson;

    /** 创建时间 */
    private LocalDateTime createdAt;

    public static final String TYPE_BEFORE = "BEFORE";
    public static final String TYPE_AFTER = "AFTER";
}
