package com.abtk.product.common.datasource;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/24 15:57
 *
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceType> CONTEXT = new ThreadLocal<>();

    public static void set(DataSourceType type) {
        CONTEXT.set(type);
    }

    public static DataSourceType get() {
        return CONTEXT.get() != null ? CONTEXT.get() : DataSourceType.MASTER;
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
