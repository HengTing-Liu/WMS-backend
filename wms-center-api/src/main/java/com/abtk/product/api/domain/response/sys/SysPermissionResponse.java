package com.abtk.product.api.domain.response.sys;

import com.abtk.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 权限管理响应出参
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限管理响应出参")
public class SysPermissionResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long permissionId;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String permissionName;

    /**
     * 权限编码（唯一标识）
     */
    @Schema(description = "权限编码")
    private String permissionCode;

    /**
     * 权限类型：1=菜单，2=按钮，3=接口
     */
    @Schema(description = "权限类型：1=菜单，2=按钮，3=接口")
    private Integer permissionType;

    /**
     * 上级权限ID
     */
    @Schema(description = "上级权限ID")
    private Long parentId;

    /**
     * 上级权限名称
     */
    @Schema(description = "上级权限名称")
    private String parentName;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 组件路径
     */
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
    @Schema(description = "备注")
    private String remarks;

    /**
     * 子权限列表
     */
    @Schema(description = "子权限列表")
    private List<SysPermissionResponse> children;
}
