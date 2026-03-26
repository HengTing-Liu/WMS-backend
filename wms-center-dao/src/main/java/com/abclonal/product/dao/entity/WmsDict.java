package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 字典管理表(WmsDict)实体类
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典管理表")
public class WmsDict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 字典编码（业务标识，唯一）
     */
    @Excel(name = "字典编码", type = Type.ALL)
    @Schema(description = "字典编码（业务标识，唯一）")
    private String dictCode;

    /**
     * 字典名称
     */
    @Excel(name = "字典名称", type = Type.ALL)
    @Schema(description = "字典名称")
    private String dictName;

    /**
     * 字典类型：system=系统字典，custom=自定义
     */
    @Excel(name = "字典类型", type = Type.ALL)
    @Schema(description = "字典类型：system=系统字典，custom=自定义")
    private String dictType;

    /**
     * 是否启用：0=禁用，1=启用
     */
    @Excel(name = "状态", type = Type.ALL, readConverterExp = "0=禁用,1=启用")
    @Schema(description = "是否启用：0=禁用，1=启用")
    private Integer isEnabled;

    /**
     * 是否删除：0=正常，1=已删除
     */
    @Schema(description = "是否删除：0=正常，1=已删除")
    private Integer isDeleted;

    /**
     * 备注（使用BaseEntity的remark字段）
     */
}
