package com.abtk.product.common.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/24 15:57
 *
 */
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType type = DataSourceContextHolder.get();
        log.info("【DataSource Router】当前选择数据源: {}", type); // 👈 关键日志！
        return type;
    }
}
