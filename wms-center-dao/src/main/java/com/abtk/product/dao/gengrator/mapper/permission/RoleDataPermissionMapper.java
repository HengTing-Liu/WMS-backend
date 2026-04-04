package com.abtk.product.dao.gengrator.mapper.permission;

import com.abtk.product.dao.gengrator.entity.permission.RoleDataPermission;

import java.util.List;

/**
 * 角色数据权限 数据层
 *
 * @author backend1
 */
public interface RoleDataPermissionMapper {

    /**
     * 查询角色数据权限信息
     *
     * @param id 角色数据权限主键
     * @return 角色数据权限信息
     */
    public RoleDataPermission selectRoleDataPermissionById(Long id);

    /**
     * 根据角色ID和表编码查询角色数据权限
     *
     * @param roleId    角色ID
     * @param tableCode 表编码
     * @return 角色数据权限信息
     */
    public RoleDataPermission selectByRoleIdAndTableCode(Long roleId, String tableCode);

    /**
     * 查询角色数据权限列表
     *
     * @param roleDataPermission 角色数据权限信息
     * @return 角色数据权限集合
     */
    public List<RoleDataPermission> selectRoleDataPermissionList(RoleDataPermission roleDataPermission);

    /**
     * 根据角色ID查询角色数据权限列表
     *
     * @param roleId 角色ID
     * @return 角色数据权限集合
     */
    public List<RoleDataPermission> selectByRoleId(Long roleId);

    /**
     * 根据角色ID数组批量查询角色数据权限列表
     *
     * @param roleIds 角色ID数组
     * @return 角色数据权限集合
     */
    public List<RoleDataPermission> selectByRoleIds(Long[] roleIds);

    /**
     * 新增角色数据权限
     *
     * @param roleDataPermission 角色数据权限信息
     * @return 结果
     */
    public int insertRoleDataPermission(RoleDataPermission roleDataPermission);

    /**
     * 修改角色数据权限
     *
     * @param roleDataPermission 角色数据权限信息
     * @return 结果
     */
    public int updateRoleDataPermission(RoleDataPermission roleDataPermission);

    /**
     * 删除角色数据权限
     *
     * @param id 角色数据权限主键
     * @return 结果
     */
    public int deleteRoleDataPermissionById(Long id);

    /**
     * 批量删除角色数据权限
     *
     * @param ids 需要删除的角色数据权限主键集合
     * @return 结果
     */
    public int deleteRoleDataPermissionByIds(Long[] ids);

    /**
     * 根据角色ID和表编码删除角色数据权限
     *
     * @param roleId    角色ID
     * @param tableCode 表编码
     * @return 结果
     */
    public int deleteByRoleIdAndTableCode(Long roleId, String tableCode);

    /**
     * 逻辑删除角色数据权限
     *
     * @param id 角色数据权限主键
     * @return 结果
     */
    public int deleteLogicById(Long id);
}
