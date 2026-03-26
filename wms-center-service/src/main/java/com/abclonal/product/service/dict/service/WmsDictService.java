package com.abclonal.product.service.dict.service;

import com.abclonal.product.dao.entity.WmsDict;

import java.util.List;

/**
 * 字典管理表(WmsDict)表服务接口
 *
 * @author backend
 * @since 2026-03-26
 */
public interface WmsDictService {

    // ==================== 查询方法 ====================

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WmsDict queryById(Long id);

    /**
     * 根据条件查询列表
     *
     * @param wmsDict 筛选条件
     * @return 对象列表
     */
    List<WmsDict> queryByCondition(WmsDict wmsDict);

    /**
     * 查询所有数据（不分页）
     *
     * @return 所有数据
     */
    List<WmsDict> listAll();

    /**
     * 统计总行数
     *
     * @param wmsDict 筛选条件
     * @return 总行数
     */
    long count(WmsDict wmsDict);

    /**
     * 校验字典编码唯一性
     *
     * @param dictCode 字典编码
     * @return 是否存在
     */
    boolean checkDictCodeUnique(String dictCode);

    // ==================== 新增方法 ====================

    /**
     * 新增数据
     *
     * @param wmsDict 实例对象
     * @return 实例对象
     */
    WmsDict insert(WmsDict wmsDict);

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param wmsDict 实例对象
     * @return 影响行数
     */
    int update(WmsDict wmsDict);

    // ==================== 删除方法 ====================

    /**
     * 通过主键逻辑删除数据
     *
     * @param id 主键
     * @param username 操作人
     * @return 是否成功
     */
    boolean logicDeleteById(Long id, String username);

    /**
     * 切换状态
     *
     * @param id 主键
     * @param isEnabled 是否启用：0=禁用，1=启用
     */
    void toggleStatus(Long id, Integer isEnabled);
}
