package com.abtk.product.api.domain.response.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 角色响应数据
 *
 * @author lht
 * @since 2026-03-11
 */
@Data
@Schema(description = "角色响应数据")
public class SysRoleResponse {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色权限编码
     */
    @Schema(description = "角色权限编码")
    private String roleKey;

    /**
     * 角色排序
     */
    @Schema(description = "角色排序")
    private Integer roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    @Schema(description = "数据范围")
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    @Schema(description = "部门树选择项是否关联显示")
    private Boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    @Schema(description = "角色状态")
    private String status;

    /**
     * 创建者
     */
    @Schema(description = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @Schema(description = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remarks;

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
     * 角色菜单权限
     */
    @Schema(description = "角色菜单权限")
    private Set<String> permissions;
}
