package com.abclonal.product.service.sys.cache;

import org.springframework.context.ApplicationEvent;

/**
 * 元数据缓存变更事件
 * <p>
 * 当元数据发生增删改时，MetaService 发布此事件，
 * MetaCacheEventListener 接收并自动刷新对应缓存。
 * <p>
 * 使用 Spring 事件机制实现缓存与数据库的同步。
 */
public class MetaCacheEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    /** 表代码 */
    private final String tableCode;

    /** 变更类型 */
    private final ChangeType changeType;

    /** 变更类型枚举 */
    public enum ChangeType {
        /** 表元数据新增/更新 */
        TABLE_UPDATED,
        /** 表元数据删除 */
        TABLE_DELETED,
        /** 字段元数据变更 */
        COLUMN_UPDATED,
        /** 操作按钮变更 */
        OPERATION_UPDATED,
        /** 全量刷新（定时任务触发） */
        FULL_REFRESH
    }

    public MetaCacheEvent(Object source, String tableCode, ChangeType changeType) {
        super(source);
        this.tableCode = tableCode;
        this.changeType = changeType;
    }

    public String getTableCode() {
        return tableCode;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    @Override
    public String toString() {
        return "MetaCacheEvent{" +
                "tableCode='" + tableCode + '\'' +
                ", changeType=" + changeType +
                '}';
    }
}
