package com.abtk.product.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单分组元数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormGroupMeta extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String tableCode;
    private String groupCode;
    private String groupTitle;
    private String groupType;
    private Integer sortOrder;
    private Integer defaultOpen;
    private Integer status;
    private String remarks;
}
