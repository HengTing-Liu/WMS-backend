package com.abtk.product.service.system.service;

import com.abtk.product.api.domain.request.sys.SysRoleRequest;
import com.abtk.product.api.domain.response.sys.SysRoleResponse;
import com.abtk.product.dao.entity.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * 角色管理 Service 接口
 *
 * @author lht
 * @since 2026-03-11
 */
public interface ISysRoleService {

    /**
     * 查询角色列表
     */
    List<SysRoleResponse> selectRoleList(SysRoleRequest request);

    /**
     * 根据ID查询角色
     */
    SysRoleResponse selectRoleById(Long id);

    /**
     * 新增角色
     */
    int insertRole(SysRoleRequest request, String createBy);

    /**
     * 修改角色
     */
    int updateRole(SysRoleRequest request, String updateBy);

    /**
     * 删除角色
     */
    int deleteRoleByIds(Long[] ids, String updateBy);

    /**
     * 校验角色名称唯一性
     */
    boolean checkRoleNameUnique(SysRoleRequest request);

    /**
     * 校验角色权限编码唯一性
     */
    boolean checkRoleKeyUnique(SysRoleRequest request);

    /**
     * 分配角色菜单权限
     */
    int assignRoleMenu(SysRoleRequest request, String updateBy);

    /**
     * 根据用户ID查询角色列表
     */
    List<SysRoleResponse> selectRolesByUserId(Long userId);

    /**
     * 查询所有角色
     */
    List<SysRoleResponse> selectRoleAll();

    /**
     * 修改角色状态
     */
    int updateRoleStatus(SysRoleRequest request, String updateBy);

    /**
     * 根据用户ID查询角色权限（兼容原有功能）
     */
    Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 统计用户使用该角色的数量
     */
    int countUserRoleByRoleId(Long id);

    /**
     * 取消授权用户角色
     */
    int deleteAuthUser(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     */
    int deleteAuthUsers(Long id, Long[] userIds);

    /**
     * 批量选择授权用户角色
     */
    int insertAuthUsers(Long id, Long[] userIds);

    /**
     * 校验角色是否有数据权限
     *
     * @param roleIds 角色id
     */
    void checkRoleDataScope(Long... ids);
}
