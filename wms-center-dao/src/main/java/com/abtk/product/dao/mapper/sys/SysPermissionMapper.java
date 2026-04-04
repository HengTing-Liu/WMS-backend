package com.abtk.product.dao.mapper.sys;

import com.abtk.product.dao.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限管理表(SysPermission)表数据库访问层
 *
 * @author backend
 * @since 2026-03-26
 */
public interface SysPermissionMapper {

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
     * @param permission 查询条件
     * @return 对象列表
     */
    List<SysPermission> queryAll(SysPermission permission);

    /**
     * 查询所有数据（不分页）
     *
     * @return 所有数据
     */
    List<SysPermission> listAll();

    /**
     * 统计总行数
     *
     * @param permission 查询条件
     * @return 总行数
     */
    long count(SysPermission permission);

    /**
     * 新增数据
     *
     * @param permission 实例对象
     * @return 影响行数
     */
    int insert(SysPermission permission);

    /**
     * 修改数据
     *
     * @param permission 实例对象
     * @return 影响行数
     */
    int update(SysPermission permission);

    /**
     * 通过主键逻辑删除数据
     *
     * @param permissionId 主键
     * @param username 操作人
     * @return 影响行数
     */
    int deleteById(@Param("permissionId") Long permissionId, @Param("username") String username);

    /**
     * 切换状态
     *
     * @param permissionId 主键
     * @param status 权限状态：0=正常，1=停用
     * @return 影响行数
     */
    int toggleStatus(@Param("permissionId") Long permissionId, @Param("status") String status);

    /**
     * 校验权限编码唯一性
     *
     * @param permissionCode 权限编码
     * @return 结果
     */
    SysPermission checkPermissionCodeUnique(@Param("permissionCode") String permissionCode);

    /**
     * 校验权限名称唯一性
     *
     * @param permissionName 权限名称
     * @param parentId 上级ID
     * @return 结果
     */
    SysPermission checkPermissionNameUnique(@Param("permissionName") String permissionName, @Param("parentId") Long parentId);
}
