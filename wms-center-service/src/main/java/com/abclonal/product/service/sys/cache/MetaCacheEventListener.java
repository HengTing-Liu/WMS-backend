package com.abclonal.product.service.sys.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 元数据缓存变更事件监听器
 * <p>
 * 监听 MetaCacheEvent，异步刷新相关缓存。
 * 所有操作均为异步，不阻塞主业务流程。
 */
@Component
public class MetaCacheEventListener {

    private static final Logger log = LoggerFactory.getLogger(MetaCacheEventListener.class);

    // 注意：这里注入的是接口而非具体实现，避免循环依赖
    // MetaCacheServiceImpl 在初始化时会调用自身发布的事件，
    // 所以监听器通过延迟注入（lazy）来避免循环依赖
    private volatile MetaCacheService metaCacheService;

    @Async("metaCacheExecutor")
    @EventListener
    public void handleMetaCacheEvent(MetaCacheEvent event) {
        log.info("收到缓存变更事件: {}", event);

        // 延迟获取 MetaCacheService（避免启动时循环依赖）
        if (metaCacheService == null) {
            try {
                metaCacheService = SpringContextHolder.getBean(MetaCacheService.class);
            } catch (Exception e) {
                log.warn("获取 MetaCacheService 失败，事件处理跳过", e);
                return;
            }
        }

        String tableCode = event.getTableCode();
        MetaCacheEvent.ChangeType changeType = event.getChangeType();

        try {
            switch (changeType) {
                case TABLE_UPDATED:
                case COLUMN_UPDATED:
                case OPERATION_UPDATED:
                    // 单表变更：只刷新该表及其相关缓存
                    if (tableCode != null) {
                        metaCacheService.invalidate(tableCode);
                        log.info("已刷新单表缓存 tableCode={}", tableCode);
                    }
                    break;

                case TABLE_DELETED:
                    // 删除时刷新全量列表
                    if (tableCode != null) {
                        metaCacheService.invalidate(tableCode);
                        metaCacheService.invalidateAllTables();
                        log.info("已刷新删除表的缓存 tableCode={}", tableCode);
                    }
                    break;

                case FULL_REFRESH:
                    // 全量刷新已完成，仅记录日志
                    log.info("全量刷新完成，事件已处理");
                    break;

                default:
                    log.warn("未知变更类型: {}", changeType);
            }
        } catch (Exception e) {
            log.error("处理缓存变更事件失败: {}", event, e);
        }
    }
}
