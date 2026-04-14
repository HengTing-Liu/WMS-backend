package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 表元数据管理表(WmsTableMeta)实体类
 *
 * @author backend
 * @since 2026-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "表元数据管理表")
public class WmsTableMeta extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 表编码（业务标识，唯一）
     */
    @Excel(name = "表编码", type = Type.ALL)
    @Schema(description = "表编码（业务标识，唯一）")
    private String tableCode;

    /**
     * 表名称
     */
    @Excel(name = "表名称", type = Type.ALL)
    @Schema(description = "表名称")
    private String tableName;

    /**
     * 所属模块
     */
    @Excel(name = "所属模块", type = Type.ALL)
    @Schema(description = "所属模块")
    private String module;

    /**
     * 实体类名
     */
    @Excel(name = "实体类名", type = Type.ALL)
    @Schema(description = "实体类名")
    private String entityClass;

    /**
     * 服务类名
     */
    @Excel(name = "服务类名", type = Type.ALL)
    @Schema(description = "服务类名")
    private String serviceClass;

    /**
     * 权限标识
     */
    @Excel(name = "权限标识", type = Type.ALL)
    @Schema(description = "权限标识")
    private String permissionCode;

    /**
     * 默认页大小
     */
    @Excel(name = "默认页大小", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "默认页大小")
    private Integer pageSize;

    /**
     * 是否树形：0=否，1=是
     */
    @Excel(name = "是否树形", type = Type.ALL, readConverterExp = "0=否,1=是")
    @Schema(description = "是否树形：0=否，1=是")
    private Integer isTree;

    /**
     * 状态：0=禁用，1=启用
     */
    @Excel(name = "状态", type = Type.ALL, readConverterExp = "0=禁用,1=启用")
    @Schema(description = "状态：0=禁用，1=启用")
    private Integer status;

    /**
     * 是否有数据权限：0=否，1=是
     */
    @Excel(name = "是否有数据权限", type = Type.ALL, readConverterExp = "0=否,1=是")
    @Schema(description = "是否有数据权限：0=否，1=是")
    private Integer hasDataPermission;

    /**
     * 权限字段
     */
    @Excel(name = "权限字段", type = Type.ALL)
    @Schema(description = "权限字段")
    private String permissionField;

    /**
     * 权限范围
     */
    @Excel(name = "权限范围", type = Type.ALL)
    @Schema(description = "权限范围")
    private String permissionScope;

    /**
     * 备注
     */
    @Excel(name = "备注", type = Type.ALL)
    @Schema(description = "备注")
    private String remarks;
}
