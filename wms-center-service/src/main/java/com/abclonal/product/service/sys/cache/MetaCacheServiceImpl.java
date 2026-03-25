package com.abclonal.product.service.sys.cache;

import com.abclonal.product.common.exception.ServiceException;
import com.abclonal.product.dao.entity.ColumnMeta;
import com.abclonal.product.dao.entity.TableMeta;
import com.abclonal.product.dao.entity.TableOperation;
import com.abclonal.product.dao.mapper.ColumnMetaMapper;
import com.abclonal.product.dao.mapper.TableMetaMapper;
import com.abclonal.product.dao.mapper.TableOperationMapper;
import com.abclonal.product.service.sys.cache.dto.CacheStatsDTO;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 元数据缓存服务实现
 * <p>
 * 三层更新机制：
 * 1. 定时刷新：每 10 分钟自动全量刷新
 * 2. 手动刷新：API 触发
 * 3. 变更触发：Spring 事件驱动自动失效
 */
@Service
public class MetaCacheServiceImpl implements MetaCacheService {

    private static final Logger log = LoggerFactory.getLogger(MetaCacheServiceImpl.class);

    /** 缓存 Key 前缀 */
    private static final String KEY_TABLE = "table:";
    private static final String KEY_ALL_TABLES = "all-tables";
    private static final String KEY_MODULE_PREFIX = "module:";

    @Autowired
    private Cache<String, Object> tableMetaCache;

    @Autowired
    private Cache<String, List<?>> columnMetaCache;

    @Autowired
    private Cache<String, List<?>> operationCache;

    @Autowired
    private Cache<String, List<?>> allTablesCache;

    @Autowired
    private Cache<String, List<?>> moduleTablesCache;

    @Autowired
    private TableMetaMapper tableMetaMapper;

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Autowired
    private TableOperationMapper tableOperationMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // ==================== 缓存读写 ====================

    @Override
    public Object getTableMeta(String tableCode) {
        String key = KEY_TABLE + tableCode;
        Object cached = tableMetaCache.getIfPresent(key);

        if (cached != null) {
            log.debug("缓存命中 tableCode={}", tableCode);
            return cached;
        }

        log.debug("缓存未命中，加载数据库 tableCode={}", tableCode);
        TableMeta tableMeta = tableMetaMapper.selectByTableCode(tableCode);

        if (tableMeta == null) {
            // 防缓存穿透：存一个空对象占位，5分钟后过期
            tableMetaCache.put(key, new NullPlaceholder(tableCode));
            throw new ServiceException("表元数据不存在: " + tableCode);
        }

        // 填充关联数据
        List<ColumnMeta> columns = columnMetaMapper.selectByTableCode(tableCode);
        List<TableOperation> operations = tableOperationMapper.selectByTableCode(tableCode);
        tableMeta.setColumns(columns);
        tableMeta.setOperations(operations);

        tableMetaCache.put(key, tableMeta);
        return tableMeta;
    }

    @Override
    public List<?> getColumnMetaList(String tableCode) {
        String key = KEY_TABLE + tableCode;
        List<?> cached = columnMetaCache.getIfPresent(key);

        if (cached != null) {
            return cached;
        }

        List<ColumnMeta> columns = columnMetaMapper.selectByTableCode(tableCode);
        columnMetaCache.put(key, columns);
        return columns;
    }

    @Override
    public List<?> getOperationList(String tableCode) {
        String key = KEY_TABLE + tableCode;
        List<?> cached = operationCache.getIfPresent(key);

        if (cached != null) {
            return cached;
        }

        List<TableOperation> operations = tableOperationMapper.selectByTableCode(tableCode);
        operationCache.put(key, operations);
        return operations;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<?> listAllTables() {
        List<?> cached = allTablesCache.getIfPresent(KEY_ALL_TABLES);

        if (cached != null) {
            return cached;
        }

        List<TableMeta> tables = tableMetaMapper.selectAll();
        allTablesCache.put(KEY_ALL_TABLES, tables);
        return tables;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<?> listTablesByModule(String module) {
        String key = KEY_MODULE_PREFIX + module;
        List<?> cached = moduleTablesCache.getIfPresent(key);

        if (cached != null) {
            return cached;
        }

        List<TableMeta> tables = tableMetaMapper.selectByModule(module);
        moduleTablesCache.put(key, tables);
        return tables;
    }

    // ==================== 缓存失效 ====================

    @Override
    public void invalidate(String tableCode) {
        String key = KEY_TABLE + tableCode;
        tableMetaCache.invalidate(key);
        columnMetaCache.invalidate(key);
        operationCache.invalidate(key);
        allTablesCache.invalidate(KEY_ALL_TABLES);
        log.info("缓存失效 tableCode={}", tableCode);
    }

    @Override
    public void invalidateColumns(String tableCode) {
        String key = KEY_TABLE + tableCode;
        columnMetaCache.invalidate(key);
    }

    @Override
    public void invalidateOperations(String tableCode) {
        String key = KEY_TABLE + tableCode;
        operationCache.invalidate(key);
    }

    @Override
    public void invalidateAllTables() {
        allTablesCache.invalidate(KEY_ALL_TABLES);
        moduleTablesCache.invalidateAll();
        log.info("全量表列表缓存已失效");
    }

    @Override
    public void invalidateModule(String module) {
        String key = KEY_MODULE_PREFIX + module;
        moduleTablesCache.invalidate(key);
    }

    @Override
    public void invalidateAll() {
        tableMetaCache.invalidateAll();
        columnMetaCache.invalidateAll();
        operationCache.invalidateAll();
        allTablesCache.invalidateAll();
        moduleTablesCache.invalidateAll();
        log.info("所有元数据缓存已清空");
    }

    // ==================== 缓存刷新 ====================

    @Override
    public void refreshAll() {
        log.info("开始全量刷新元数据缓存...");
        long start = System.currentTimeMillis();

        invalidateAll();

        // 重新加载全量表列表
        List<TableMeta> tables = tableMetaMapper.selectAll();
        allTablesCache.put(KEY_ALL_TABLES, tables);

        long elapsed = System.currentTimeMillis() - start;
        log.info("全量刷新完成，耗时={}ms，表数量={}", elapsed, tables.size());

        // 发布事件通知
        eventPublisher.publishEvent(new MetaCacheEvent(this, null, MetaCacheEvent.ChangeType.FULL_REFRESH));
    }

    @Override
    public void refresh(String tableCode) {
        log.info("刷新单表缓存 tableCode={}", tableCode);
        invalidate(tableCode);
    }

    // ==================== 缓存统计 ====================

    @Override
    public CacheStatsDTO getStats() {
        CacheStatsDTO dto = new CacheStatsDTO();

        dto.setTableMeta(buildEntry(tableMetaCache));
        dto.setColumnMeta(buildEntry(columnMetaCache));
        dto.setOperation(buildEntry(operationCache));
        dto.setAllTables(buildEntry(allTablesCache));
        dto.setModuleTables(buildEntry(moduleTablesCache));

        // 计算总体命中率
        long totalHit = dto.getTableMeta().getHitCount()
                + dto.getColumnMeta().getHitCount()
                + dto.getOperation().getHitCount();
        long totalMiss = dto.getTableMeta().getMissCount()
                + dto.getColumnMeta().getMissCount()
                + dto.getOperation().getMissCount();
        long total = totalHit + totalMiss;
        dto.setOverallHitRate(total == 0 ? 0.0 : (double) totalHit / total);

        return dto;
    }

    private CacheStatsDTO.CacheStatEntry buildEntry(Cache<?, ?> cache) {
        CacheStatsDTO.CacheStatEntry entry = new CacheStatsDTO.CacheStatEntry();
        com.github.benmanes.caffeine.cache.stats.CacheStats stats = cache.stats();
        entry.setHitCount(stats.hitCount());
        entry.setMissCount(stats.missCount());
        entry.setEvictionCount(stats.evictionCount());
        entry.setSize(cache.estimatedSize());
        return entry;
    }

    // ==================== 缓存预热 ====================

    /**
     * 应用启动时预热缓存（异步，不阻塞启动）
     */
    @PostConstruct
    public void warmUp() {
        new Thread(() -> {
            try {
                // 延迟 30 秒，等应用完全启动后再预热
                TimeUnit.SECONDS.sleep(30);
                log.info("开始缓存预热...");
                long start = System.currentTimeMillis();
                List<TableMeta> tables = tableMetaMapper.selectAll();
                allTablesCache.put(KEY_ALL_TABLES, tables);
                log.info("缓存预热完成，耗时={}ms，预热表数量={}", System.currentTimeMillis() - start, tables.size());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("缓存预热被中断");
            }
        }).start();
    }

    // ==================== 定时刷新 ====================

    /**
     * 每 10 分钟定时全量刷新缓存
     * 使用 fixedRate 确保即使上次任务未完成也不会并发执行
     */
    @Scheduled(fixedRateString = "${meta.cache.refresh-interval:600000}",
              initialDelayString = "${meta.cache.refresh-initial-delay:600000}")
    public void scheduledRefresh() {
        log.info("定时任务触发，开始刷新元数据缓存...");
        try {
            refreshAll();
        } catch (Exception e) {
            log.error("定时刷新缓存失败", e);
        }
    }

    // ==================== 空对象占位符（防缓存穿透） ====================

    /**
     * 占位对象：用于缓存不存在的数据，防止缓存穿透
     */
    private static class NullPlaceholder {
        private final String tableCode;
        private final long createTime = System.currentTimeMillis();

        NullPlaceholder(String tableCode) {
            this.tableCode = tableCode;
        }

        public String getTableCode() { return tableCode; }
        public long getCreateTime() { return createTime; }
    }
}
