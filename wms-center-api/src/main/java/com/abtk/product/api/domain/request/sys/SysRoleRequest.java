package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 角色请求入参
 *
 * @author lht
 * @since 2026-03-11
 */
@Data
@Schema(description = "角色请求入参")
public class SysRoleRequest extends BaseRequest {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色权限编码
     */
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    @Schema(description = "角色权限编码")
    private String roleKey;

    /**
     * 角色排序
     */
    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "角色排序")
    private Integer roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    @Schema(description = "数据范围")
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示）
     */
    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示）
     */
    @Schema(description = "部门树选择项是否关联显示")
    private Boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    @Schema(description = "角色状态")
    private String status;

    /**
     * 菜单ID数组
     */
    @Schema(description = "菜单ID数组")
    private Long[] menuIds;

    /**
     * 部门ID数组（数据权限自定义时使用）
     */
    @Schema(description = "部门ID数组（数据权限自定义时使用）")
    private Long[] deptIds;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
