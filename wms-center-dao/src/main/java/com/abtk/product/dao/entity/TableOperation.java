package com.abtk.product.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作按钮配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableOperation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 表标识 */
    private String tableCode;

    /** 操作标识 */
    private String operationCode;

    /** 操作名称 */
    private String operationName;

    /** 操作类型: button/link */
    private String operationType;

    /** 图标 */
    private String icon;

    /** 权限标识 */
    private String permission;

    /** 位置: toolbar-工具栏 row-行内 */
    private String position;

    /** 排序号 */
    private Integer sortOrder;

    /** 状态: 0-禁用 1-启用 */
    private Integer status;

    /** 关联 sys_menu F类按钮ID（打通 UI按钮配置 与 权限点） */
    private Long menuId;

    /** 事件类型: builtin|api|download|redirect|modal|drawer */
    private String eventType;

    /** 事件配置JSON */
    private String eventConfig;

    /** 确认提示消息 */
    private String confirmMessage;
}
