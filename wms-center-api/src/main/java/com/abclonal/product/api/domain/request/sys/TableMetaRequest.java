package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 表元数据保存请求
 */
@Data
@Schema(description = "表元数据保存请求")
public class TableMetaRequest extends BaseRequest {

    @Schema(description = "主键ID(新增时为空)")
    private Long id;

    @NotBlank(message = "表标识不能为空")
    @Schema(description = "表标识(小写下划线)", required = true)
    private String tableCode;

    @NotBlank(message = "表名称不能为空")
    @Schema(description = "表名称", required = true)
    private String tableName;

    @Schema(description = "所属模块(base/wms/sys)")
    private String module;

    @Schema(description = "实体类全路径")
    private String entityClass;

    @Schema(description = "Service类全路径")
    private String serviceClass;

    @Schema(description = "权限码")
    private String permissionCode;

    @Schema(description = "默认分页大小")
    private Integer pageSize = 20;

    @Schema(description = "是否树形表")
    private Boolean isTree = false;

    @Schema(description = "状态(0-禁用,1-启用)")
    private Integer status = 1;

    @Schema(description = "备注")
    private String remark;
}
