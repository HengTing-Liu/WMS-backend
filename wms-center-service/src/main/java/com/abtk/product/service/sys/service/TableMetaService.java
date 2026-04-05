package com.abtk.product.service.sys.service;

import com.abtk.product.dao.entity.TableMeta;

import java.util.List;

/**
 * 表元数据服务接口
 * Service层只做简单CRUD，复杂业务在Biz层处理
 *
 * @author backend
 * @since 2026-04-06
 */
public interface TableMetaService {

    /**
     * 分页查询表元数据列表
     *
     * @param condition 查询条件（Entity）
     * @return 表元数据列表
     */
    List<TableMeta> list(TableMeta condition);

    /**
     * 根据ID查询表元数据详情
     *
     * @param id 表元数据ID
     * @return 表元数据详情
     */
    TableMeta getById(Long id);

    /**
     * 根据表编码查询表元数据
     *
     * @param tableCode 表编码
     * @return 表元数据详情
     */
    TableMeta getByTableCode(String tableCode);

    /**
     * 新增表元数据
     *
     * @param tableMeta 表元数据信息（Entity）
     * @return 新增表元数据ID
     */
    Long create(TableMeta tableMeta);

    /**
     * 更新表元数据
     *
     * @param id 表元数据ID
     * @param tableMeta 表元数据信息（Entity）
     */
    void update(Long id, TableMeta tableMeta);

    /**
     * 删除表元数据
     *
     * @param id 表元数据ID
     */
    void delete(Long id);

    /**
     * 切换表元数据状态（启用/禁用）
     *
     * @param id 表元数据ID
     * @param status 状态（0-禁用，1-启用）
     */
    void toggleStatus(Long id, Integer status);

    /**
     * 查询所有表元数据（不分页）
     *
     * @return 表元数据列表
     */
    List<TableMeta> listAll();

    /**
     * 根据模块查询表元数据
     *
     * @param module 所属模块
     * @return 表元数据列表
     */
    List<TableMeta> listByModule(String module);
}
