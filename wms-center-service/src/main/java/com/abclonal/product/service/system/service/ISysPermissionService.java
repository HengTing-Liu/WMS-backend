package com.abclonal.product.service.system.service;

import com.abclonal.product.dao.entity.SysPermission;

import java.util.List;

/**
 * 权限管理表(SysPermission)表服务接口
 *
 * @author backend
 * @since 2026-03-26
 */
public interface ISysPermissionService {

    // ==================== 查询方法 ====================

    /**
     * 通过ID查询单条数据
     *
     * @param permissionId 主键
     * @return 实例对象
     */
    SysPermission queryById(Long permissionId);

    /**
     * 根据条件查询列表
     *
     * @param permission 筛选条件
     * @return 对象列表
     */
    List<SysPermission> queryByCondition(SysPermission permission);

    /**
     * 查询所有数据（不分页）
     *
     * @return 所有数据
     */
    List<SysPermission> listAll();

    /**
     * 统计总行数
     *
     * @param permission 筛选条件
     * @return 总行数
     */
    long count(SysPermission permission);

    /**
     * 校验权限编码唯一性
     *
     * @param permissionCode 权限编码
     * @return 是否存在
     */
    boolean checkPermissionCodeUnique(String permissionCode);

    /**
     * 校验权限名称唯一性
     *
     * @param permissionName 权限名称
     * @param parentId 上级ID
     * @return 是否存在
     */
    boolean checkPermissionNameUnique(String permissionName, Long parentId);

    // ==================== 新增方法 ====================

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 实例对象
     */
    SysPermission insert(SysPermission permission);

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param permission 实例对象
     * @return 影响行数
     */
    int update(SysPermission permission);

    // ==================== 删除方法 ====================

    /**
     * 通过主键逻辑删除数据
     *
     * @param permissionId 主键
     * @param username 操作人
     * @return 是否成功
     */
    boolean logicDeleteById(Long permissionId, String username);

    /**
     * 切换状态
     *
     * @param permissionId 主键
     * @param status 权限状态：0=正常，1=停用
     */
    void toggleStatus(Long permissionId, String status);
}
