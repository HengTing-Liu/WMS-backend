package com.abclonal.product.service.sys.service;

import com.abclonal.product.dao.entity.Storage;

import java.util.List;

/**
 * 库区档案服务接口
 * WMS0050 库区管理
 * Service层只做简单CRUD，复杂业务在Biz层处理
 */
public interface StorageService {

    /**
     * 查询库区列表
     *
     * @param condition 查询条件（Entity）
     * @return 库区列表
     */
    List<Storage> list(Storage condition);

    /**
     * 根据ID查询库区详情
     *
     * @param id 库区ID
     * @return 库区详情
     */
    Storage getById(Long id);

    /**
     * 新增库区
     *
     * @param storage 库区信息（Entity）
     * @return 新增库区ID
     */
    Long create(Storage storage);

    /**
     * 更新库区
     *
     * @param id 库区ID
     * @param storage 库区信息（Entity）
     */
    void update(Long id, Storage storage);

    /**
     * 删除库区（逻辑删除）
     *
     * @param id 库区ID
     */
    void delete(Long id);

    /**
     * 切换库区状态
     *
     * @param id 库区ID
     * @param enabled 状态
     */
    void toggleStatus(Long id, Integer enabled);

    /**
     * 查询所有库区（不分页）
     *
     * @return 库区列表
     */
    List<Storage> listAll();
}
