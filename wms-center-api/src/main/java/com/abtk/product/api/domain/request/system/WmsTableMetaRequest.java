package com.abtk.product.api.domain.request.system;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 表元数据管理请求入参
 *
 * @author backend
 * @since 2026-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "表元数据管理请求入参")
public class WmsTableMetaRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 表编码（业务标识，唯一）
     */
    @NotBlank(message = "表编码不能为空")
    @Size(max = 100, message = "表编码长度不能超过100个字符")
    @Schema(description = "表编码（业务标识，唯一）")
    private String tableCode;

    /**
     * 表名称
     */
    @NotBlank(message = "表名称不能为空")
    @Size(max = 200, message = "表名称长度不能超过200个字符")
    @Schema(description = "表名称")
    private String tableName;

    /**
     * 所属模块
     */
    @NotBlank(message = "所属模块不能为空")
    @Size(max = 50, message = "所属模块长度不能超过50个字符")
    @Schema(description = "所属模块")
    private String module;

    /**
     * 实体类名
     */
    @Size(max = 200, message = "实体类名长度不能超过200个字符")
    @Schema(description = "实体类名")
    private String entityClass;

    /**
     * 服务类名
     */
    @Size(max = 200, message = "服务类名长度不能超过200个字符")
    @Schema(description = "服务类名")
    private String serviceClass;

    /**
     * 权限标识
     */
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    @Schema(description = "权限标识")
    private String permissionCode;

    /**
     * 默认页大小
     */
    @Schema(description = "默认页大小")
    private Integer pageSize;

    /**
     * 是否树形：0=否，1=是
     */
    @Schema(description = "是否树形：0=否，1=是")
    private Integer isTree;

    /**
     * 状态：0=禁用，1=启用
     */
    @Schema(description = "状态：0=禁用，1=启用")
    private Integer status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注")
    private String remarks;

    /**
     * 是否有数据权限：0=否，1=是
     */
    @Schema(description = "是否有数据权限：0=否，1=是")
    private Integer hasDataPermission;

    /**
     * 权限字段
     */
    @Size(max = 64, message = "权限字段长度不能超过64个字符")
    @Schema(description = "权限字段")
    private String permissionField;

    /**
     * 权限范围
     */
    @Size(max = 32, message = "权限范围长度不能超过32个字符")
    @Schema(description = "权限范围")
    private String permissionScope;
}
