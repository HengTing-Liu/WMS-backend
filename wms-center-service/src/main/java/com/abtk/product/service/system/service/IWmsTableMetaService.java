package com.abtk.product.service.system.service;

import com.abtk.product.dao.entity.WmsTableMeta;

import java.util.List;

/**
 * 表元数据管理表(WmsTableMeta)表服务接口
 *
 * @author backend
 * @since 2026-04-06
 */
public interface IWmsTableMetaService {

    // ==================== 查询方法 ====================

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WmsTableMeta queryById(Long id);

    /**
     * 通过表编码查询单条数据
     *
     * @param tableCode 表编码
     * @return 实例对象
     */
    WmsTableMeta queryByCode(String tableCode);

    /**
     * 根据条件查询列表
     *
     * @param wmsTableMeta 筛选条件
     * @return 对象列表
     */
    List<WmsTableMeta> queryByCondition(WmsTableMeta wmsTableMeta);

    /**
     * 查询所有数据（不分页）
     *
     * @return 所有数据
     */
    List<WmsTableMeta> listAll();

    /**
     * 统计总行数
     *
     * @param wmsTableMeta 筛选条件
     * @return 总行数
     */
    long count(WmsTableMeta wmsTableMeta);

    /**
     * 校验表编码唯一性
     *
     * @param tableCode 表编码
     * @return 是否存在
     */
    boolean checkTableCodeUnique(String tableCode);

    /**
     * 检查表是否有关联字段
     *
     * @param tableId 表ID
     * @return 是否有关联字段
     */
    boolean hasRelatedFields(Long tableId);

    // ==================== 新增方法 ====================

    /**
     * 新增数据
     *
     * @param wmsTableMeta 实例对象
     * @return 实例对象
     */
    WmsTableMeta insert(WmsTableMeta wmsTableMeta);

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param wmsTableMeta 实例对象
     * @return 影响行数
     */
    int update(WmsTableMeta wmsTableMeta);

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
     * @param status 状态：0=禁用，1=启用
     */
    void toggleStatus(Long id, Integer status);
}
