package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 权限管理请求入参
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限管理请求入参")
public class SysPermissionRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long permissionId;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50个字符")
    @Schema(description = "权限名称")
    private String permissionName;

    /**
     * 权限编码（唯一标识）
     */
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    @Schema(description = "权限编码")
    private String permissionCode;

    /**
     * 权限类型：1=菜单，2=按钮，3=接口
     */
    @NotNull(message = "权限类型不能为空")
    @Schema(description = "权限类型：1=菜单，2=按钮，3=接口")
    private Integer permissionType;

    /**
     * 上级权限ID
     */
    @Schema(description = "上级权限ID")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Size(max = 200, message = "路由地址长度不能超过200个字符")
    @Schema(description = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @Size(max = 255, message = "组件路径长度不能超过255个字符")
    @Schema(description = "组件路径")
    private String component;

    /**
     * 路由参数
     */
    @Schema(description = "路由参数")
    private String query;

    /**
     * 权限状态：0=正常，1=停用
     */
    @Schema(description = "权限状态：0=正常，1=停用")
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注")
    private String remark;
}
