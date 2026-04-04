package com.abtk.product.dao.gengrator.mapper.permission;

import com.abtk.product.dao.gengrator.entity.permission.DeptDataPermission;

import java.util.List;

/**
 * 部门数据权限 数据层
 *
 * @author backend1
 */
public interface DeptDataPermissionMapper {

    /**
     * 查询部门数据权限信息
     *
     * @param id 部门数据权限主键
     * @return 部门数据权限信息
     */
    public DeptDataPermission selectDeptDataPermissionById(Long id);

    /**
     * 根据部门ID和表编码查询部门数据权限
     *
     * @param deptId    部门ID
     * @param tableCode 表编码
     * @return 部门数据权限信息
     */
    public DeptDataPermission selectByDeptIdAndTableCode(Long deptId, String tableCode);

    /**
     * 查询部门数据权限列表
     *
     * @param deptDataPermission 部门数据权限信息
     * @return 部门数据权限集合
     */
    public List<DeptDataPermission> selectDeptDataPermissionList(DeptDataPermission deptDataPermission);

    /**
     * 根据部门ID查询部门数据权限列表
     *
     * @param deptId 部门ID
     * @return 部门数据权限集合
     */
    public List<DeptDataPermission> selectByDeptId(Long deptId);

    /**
     * 新增部门数据权限
     *
     * @param deptDataDataPermission 部门数据权限信息
     * @return 结果
     */
    public int insertDeptDataPermission(DeptDataPermission deptDataPermission);

    /**
     * 修改部门数据权限
     *
     * @param deptDataPermission 部门数据权限信息
     * @return 结果
     */
    public int updateDeptDataPermission(DeptDataPermission deptDataPermission);

    /**
     * 删除部门数据权限
     *
     * @param id 部门数据权限主键
     * @return 结果
     */
    public int deleteDeptDataPermissionById(Long id);

    /**
     * 批量删除部门数据权限
     *
     * @param ids 需要删除的部门数据权限主键集合
     * @return 结果
     */
    public int deleteDeptDataPermissionByIds(Long[] ids);

    /**
     * 根据部门ID和表编码删除部门数据权限
     *
     * @param deptId    部门ID
     * @param tableCode 表编码
     * @return 结果
     */
    public int deleteByDeptIdAndTableCode(Long deptId, String tableCode);

    /**
     * 逻辑删除部门数据权限
     *
     * @param id 部门数据权限主键
     * @return 结果
     */
    public int deleteLogicById(Long id);
}
