package com.abtk.product.service.user.service;

import com.abtk.product.dao.entity.WmsUser;

import java.util.List;

/**
 * 用户管理表(WmsUser)表服务接口
 *
 * @author backend
 * @since 2026-03-26
 */
public interface WmsUserService {

    // ==================== 查询方法 ====================

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    WmsUser queryById(Long userId);

    /**
     * 根据条件查询列表
     *
     * @param wmsUser 筛选条件
     * @return 对象列表
     */
    List<WmsUser> queryByCondition(WmsUser wmsUser);

    /**
     * 查询所有数据（不分页）
     *
     * @return 所有数据
     */
    List<WmsUser> listAll();

    /**
     * 统计总行数
     *
     * @param wmsUser 筛选条件
     * @return 总行数
     */
    long count(WmsUser wmsUser);

    /**
     * 校验用户名唯一性
     *
     * @param userName 用户名
     * @return 是否存在
     */
    boolean checkUserNameUnique(String userName);

    // ==================== 新增方法 ====================

    /**
     * 新增数据
     *
     * @param wmsUser 实例对象
     * @return 实例对象
     */
    WmsUser insert(WmsUser wmsUser);

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param wmsUser 实例对象
     * @return 影响行数
     */
    int update(WmsUser wmsUser);

    // ==================== 删除方法 ====================

    /**
     * 通过主键逻辑删除数据
     *
     * @param userId 主键
     * @param username 操作人
     * @return 是否成功
     */
    boolean logicDeleteById(Long userId, String username);

    /**
     * 切换状态
     *
     * @param userId 主键
     * @param status 账号状态：0=正常，1=停用
     */
    void toggleStatus(Long userId, String status);
}
