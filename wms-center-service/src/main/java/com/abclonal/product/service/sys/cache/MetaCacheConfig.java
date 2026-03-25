package com.abclonal.product.service.sys.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 元数据缓存配置
 * <p>
 * 选型：Caffeine 本地缓存
 * - 内存级访问，延迟 < 1ms
 * - 支持丰富的过期策略和统计
 * - 零部署复杂度，适合单体及多实例部署
 * <p>
 * Key 规范：
 * - table:{tableCode}   → TableMeta（含 columns + operations）
 * - all-tables          → List<TableMeta>
 * - module:{moduleCode} → List<TableMeta>
 */
@Configuration
public class MetaCacheConfig {

    /** 最大缓存表数量 */
    private static final int MAX_SIZE = 1000;

    /** 默认过期时间（分钟）：定时任务每10分钟刷新，这里设为12分钟作为兜底 */
    private static final long EXPIRE_AFTER_WRITE_MINUTES = 12;

    /**
     * 主缓存：表元数据
     */
    @Bean("tableMetaCache")
    public Cache<String, Object> tableMetaCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_AFTER_WRITE_MINUTES, TimeUnit.MINUTES)
                .maximumSize(MAX_SIZE)
                .recordStats()
                .removalListener((key, value, cause) -> {
                    // 淘汰日志可在此记录
                })
                .build();
    }

    /**
     * 字段元数据缓存
     */
    @Bean("columnMetaCache")
    public Cache<String, List<?>> columnMetaCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_AFTER_WRITE_MINUTES, TimeUnit.MINUTES)
                .maximumSize(MAX_SIZE)
                .recordStats()
                .build();
    }

    /**
     * 操作按钮缓存
     */
    @Bean("operationCache")
    public Cache<String, List<?>> operationCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_AFTER_WRITE_MINUTES, TimeUnit.MINUTES)
                .maximumSize(MAX_SIZE)
                .recordStats()
                .build();
    }

    /**
     * 全量表列表缓存
     */
    @Bean("allTablesCache")
    public Cache<String, List<?>> allTablesCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_AFTER_WRITE_MINUTES, TimeUnit.MINUTES)
                .maximumSize(10)
                .recordStats()
                .build();
    }

    /**
     * 按模块查询缓存
     */
    @Bean("moduleTablesCache")
    public Cache<String, List<?>> moduleTablesCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_AFTER_WRITE_MINUTES, TimeUnit.MINUTES)
                .maximumSize(100)
                .recordStats()
                .build();
    }

    /**
     * 缓存统计容器（线程安全）
     */
    @Bean
    public Map<String, CacheStats> cacheStatsMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 缓存统计信息持有类（用于暴露统计指标）
     */
    public static class CacheStats {
        private long hitCount;
        private long missCount;
        private long evictionCount;
        private long estimatedSize;

        public long getHitCount() { return hitCount; }
        public void setHitCount(long hitCount) { this.hitCount = hitCount; }
        public long getMissCount() { return missCount; }
        public void setMissCount(long missCount) { this.missCount = missCount; }
        public long getEvictionCount() { return evictionCount; }
        public void setEvictionCount(long evictionCount) { this.evictionCount = evictionCount; }
        public long getEstimatedSize() { return estimatedSize; }
        public void setEstimatedSize(long estimatedSize) { this.estimatedSize = estimatedSize; }
    }
}
