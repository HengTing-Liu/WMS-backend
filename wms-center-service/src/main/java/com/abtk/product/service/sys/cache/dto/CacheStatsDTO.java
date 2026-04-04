package com.abtk.product.service.sys.cache.dto;

import java.io.Serializable;

/**
 * 缓存统计 DTO
 */
public class CacheStatsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 表元数据缓存统计 */
    private CacheStatEntry tableMeta;

    /** 字段元数据缓存统计 */
    private CacheStatEntry columnMeta;

    /** 操作按钮缓存统计 */
    private CacheStatEntry operation;

    /** 全量表列表缓存统计 */
    private CacheStatEntry allTables;

    /** 按模块查询缓存统计 */
    private CacheStatEntry moduleTables;

    /** 总体命中率 */
    private double overallHitRate;

    public CacheStatEntry getTableMeta() { return tableMeta; }
    public void setTableMeta(CacheStatEntry tableMeta) { this.tableMeta = tableMeta; }
    public CacheStatEntry getColumnMeta() { return columnMeta; }
    public void setColumnMeta(CacheStatEntry columnMeta) { this.columnMeta = columnMeta; }
    public CacheStatEntry getOperation() { return operation; }
    public void setOperation(CacheStatEntry operation) { this.operation = operation; }
    public CacheStatEntry getAllTables() { return allTables; }
    public void setAllTables(CacheStatEntry allTables) { this.allTables = allTables; }
    public CacheStatEntry getModuleTables() { return moduleTables; }
    public void setModuleTables(CacheStatEntry moduleTables) { this.moduleTables = moduleTables; }
    public double getOverallHitRate() { return overallHitRate; }
    public void setOverallHitRate(double overallHitRate) { this.overallHitRate = overallHitRate; }

    /**
     * 单个缓存条目的统计信息
     */
    public static class CacheStatEntry implements Serializable {
        private static final long serialVersionUID = 1L;

        private long hitCount;
        private long missCount;
        private long evictionCount;
        private long size;

        public double getHitRate() {
            long total = hitCount + missCount;
            return total == 0 ? 0.0 : (double) hitCount / total;
        }

        public long getHitCount() { return hitCount; }
        public void setHitCount(long hitCount) { this.hitCount = hitCount; }
        public long getMissCount() { return missCount; }
        public void setMissCount(long missCount) { this.missCount = missCount; }
        public long getEvictionCount() { return evictionCount; }
        public void setEvictionCount(long evictionCount) { this.evictionCount = evictionCount; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
    }
}
