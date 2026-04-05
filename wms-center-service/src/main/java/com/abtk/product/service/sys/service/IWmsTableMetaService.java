package com.abtk.product.service.sys.service;

import com.abtk.product.dao.entity.WmsTableMeta;

import java.util.List;

/**
 * 表元数据管理服务接口
 * Service层只做简单CRUD，复杂业务在Biz层处理
 *
 * @author backend
 * @since 2026-04-06
 */
public interface IWmsTableMetaService {

    /**
     * 查询表元数据列表
     *
     * @param condition 查询条件（Entity）
     * @return 表元数据列表
     */
    List<WmsTableMeta> list(WmsTableMeta condition);

    /**
     * 根据ID查询表元数据详情
     *
     * @param id 表元数据ID
     * @return 表元数据详情
     */
    WmsTableMeta getById(Long id);

    /**
     * 根据表编码查询表元数据
     *
     * @param tableCode 表编码
     * @return 表元数据详情
     */
    WmsTableMeta getByTableCode(String tableCode);

    /**
     * 新增表元数据
     *
     * @param tableMeta 表元数据信息（Entity）
     * @return 新增表元数据ID
     */
    Long create(WmsTableMeta tableMeta);

    /**
     * 更新表元数据
     *
     * @param id 表元数据ID
     * @param tableMeta 表元数据信息（Entity）
     */
    void update(Long id, WmsTableMeta tableMeta);

    /**
     * 删除表元数据（逻辑删除）
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
    List<WmsTableMeta> listAll();

    /**
     * 根据模块查询表元数据
     *
     * @param module 所属模块
     * @return 表元数据列表
     */
    List<WmsTableMeta> listByModule(String module);

    /**
     * 校验表编码唯一性
     *
     * @param tableCode 表编码
     * @param excludeId 排除的ID（更新时使用）
     * @return true-唯一，false-已存在
     */
    boolean checkTableCodeUnique(String tableCode, Long excludeId);

    /**
     * 检查表是否有关联字段
     *
     * @param tableId 表ID
     * @return true-有关联字段，false-无关联字段
     */
    boolean hasRelatedFields(Long tableId);
}
