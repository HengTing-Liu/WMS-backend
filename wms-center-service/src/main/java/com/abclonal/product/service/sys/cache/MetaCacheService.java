package com.abclonal.product.service.sys.cache;

import com.abclonal.product.service.sys.cache.dto.CacheStatsDTO;

import java.util.List;

/**
 * 元数据缓存服务接口
 */
public interface MetaCacheService {

    // ==================== 缓存读写 ====================

    /**
     * 获取表元数据（带缓存）
     * @param tableCode 表代码
     * @return TableMeta（含 columns 和 operations）
     */
    Object getTableMeta(String tableCode);

    /**
     * 获取字段元数据列表（带缓存）
     * @param tableCode 表代码
     * @return 字段列表
     */
    List<?> getColumnMetaList(String tableCode);

    /**
     * 获取操作按钮列表（带缓存）
     * @param tableCode 表代码
     * @return 操作按钮列表
     */
    List<?> getOperationList(String tableCode);

    /**
     * 获取所有表元数据（带缓存）
     * @return 表列表
     */
    List<?> listAllTables();

    /**
     * 按模块查询表元数据（带缓存）
     * @param module 模块代码
     * @return 表列表
     */
    List<?> listTablesByModule(String module);

    // ==================== 缓存失效 ====================

    /**
     * 使单表缓存失效
     * @param tableCode 表代码
     */
    void invalidate(String tableCode);

    /**
     * 使字段元数据缓存失效
     * @param tableCode 表代码
     */
    void invalidateColumns(String tableCode);

    /**
     * 使操作按钮缓存失效
     * @param tableCode 表代码
     */
    void invalidateOperations(String tableCode);

    /**
     * 使全量表列表缓存失效
     */
    void invalidateAllTables();

    /**
     * 使模块级缓存失效
     * @param module 模块代码
     */
    void invalidateModule(String module);

    /**
     * 清空所有缓存
     */
    void invalidateAll();

    // ==================== 缓存刷新 ====================

    /**
     * 全量刷新所有缓存（从数据库重新加载）
     */
    void refreshAll();

    /**
     * 刷新单表缓存
     * @param tableCode 表代码
     */
    void refresh(String tableCode);

    // ==================== 缓存统计 ====================

    /**
     * 获取缓存统计信息
     * @return 统计 DTO
     */
    CacheStatsDTO getStats();

    // ==================== 缓存预热 ====================

    /**
     * 启动时预热缓存（可选，用于减少冷启动延迟）
     */
    void warmUp();
}
