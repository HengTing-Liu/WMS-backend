package com.abtk.product.api.domain.response.sys;

import com.abtk.product.api.domain.response.BaseResponse;
import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 表元数据响应出参
 *
 * @author backend
 * @since 2026-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "表元数据响应出参")
public class TableMetaResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "表编码")
    @Schema(description = "表编码")
    private String tableCode;

    @Excel(name = "表名称")
    @Schema(description = "表名称")
    private String tableName;

    @Excel(name = "所属模块")
    @Schema(description = "所属模块")
    private String module;

    @Excel(name = "实体类名")
    @Schema(description = "实体类名")
    private String entityClass;

    @Excel(name = "服务类名")
    @Schema(description = "服务类名")
    private String serviceClass;

    @Excel(name = "权限标识")
    @Schema(description = "权限标识")
    private String permissionCode;

    @Excel(name = "默认页大小")
    @Schema(description = "默认页大小")
    private Integer pageSize;

    @Excel(name = "是否树形", readConverterExp = "0=否,1=是")
    @Schema(description = "是否树形: 0-否 1-是")
    private Integer isTree;

    @Excel(name = "状态", readConverterExp = "0=禁用,1=启用")
    @Schema(description = "状态: 0-禁用 1-启用")
    private Integer status;

    @Excel(name = "备注")
    @Schema(description = "备注")
    private String remark;

    @Excel(name = "逻辑删除字段名")
    @Schema(description = "逻辑删除字段名")
    private String isDeletedColumn;

    @Excel(name = "创建人")
    @Schema(description = "创建人")
    private String createBy;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人")
    private String updateBy;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;
}
