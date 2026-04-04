package com.abtk.product.dao.gengrator.mapper.permission;

import com.abtk.product.dao.gengrator.entity.permission.UserDataPermission;

import java.util.List;

/**
 * 个人数据权限 数据层
 *
 * @author backend1
 */
public interface UserDataPermissionMapper {

    /**
     * 查询个人数据权限信息
     *
     * @param id 个人数据权限主键
     * @return 个人数据权限信息
     */
    public UserDataPermission selectUserDataPermissionById(Long id);

    /**
     * 根据用户ID和表编码查询个人数据权限
     *
     * @param userId   用户ID
     * @param tableCode 表编码
     * @return 个人数据权限信息
     */
    public UserDataPermission selectByUserIdAndTableCode(Long userId, String tableCode);

    /**
     * 查询个人数据权限列表
     *
     * @param userDataPermission 个人数据权限信息
     * @return 个人数据权限集合
     */
    public List<UserDataPermission> selectUserDataPermissionList(UserDataPermission userDataPermission);

    /**
     * 根据用户ID查询个人数据权限列表
     *
     * @param userId 用户ID
     * @return 个人数据权限集合
     */
    public List<UserDataPermission> selectByUserId(Long userId);

    /**
     * 新增个人数据权限
     *
     * @param userDataPermission 个人数据权限信息
     * @return 结果
     */
    public int insertUserDataPermission(UserDataPermission userDataPermission);

    /**
     * 修改个人数据权限
     *
     * @param userDataPermission 个人数据权限信息
     * @return 结果
     */
    public int updateUserDataPermission(UserDataPermission userDataPermission);

    /**
     * 删除个人数据权限
     *
     * @param id 个人数据权限主键
     * @return 结果
     */
    public int deleteUserDataPermissionById(Long id);

    /**
     * 批量删除个人数据权限
     *
     * @param ids 需要删除的个人数据权限主键集合
     * @return 结果
     */
    public int deleteUserDataPermissionByIds(Long[] ids);

    /**
     * 根据用户ID和表编码删除个人数据权限
     *
     * @param userId   用户ID
     * @param tableCode 表编码
     * @return 结果
     */
    public int deleteByUserIdAndTableCode(Long userId, String tableCode);

    /**
     * 逻辑删除个人数据权限
     *
     * @param id 个人数据权限主键
     * @return 结果
     */
    public int deleteLogicById(Long id);
}
