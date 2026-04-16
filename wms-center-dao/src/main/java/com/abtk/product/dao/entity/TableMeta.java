package com.abtk.product.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 表元数据实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableMeta extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 表标识(英文) */
    private String tableCode;

    /** 表名称(中文) */
    private String tableName;

    /** 所属模块 */
    private String module;

    /** 实体类路径 */
    private String entityClass;

    /** Service类路径 */
    private String serviceClass;

    /** CRUD接口前缀 */
    private String apiPrefix;

    /** 权限码前缀 */
    private String permissionCode;

    /** 默认分页大小 */
    private Integer pageSize;

    /** 是否树形表: 0-否 1-是 */
    private Integer isTree;

    /** 状态: 0-禁用 1-启用 */
    private Integer status;

    /** 备注 */
    private String remarks;

    /** 表字段列表（非数据库字段） */
    private transient List<ColumnMeta> columns;

    /** 表操作列表（非数据库字段） */
    private transient List<TableOperation> operations;

    // 代码生成器适配方法（兼容 common.generator.TableMeta）
    public String getClassName() {
        return entityClass != null ? extractClassName(entityClass) : null;
    }

    public String getTableComment() {
        return tableName;
    }

    public String getPackageName() {
        if (entityClass != null) {
            int lastDot = entityClass.lastIndexOf('.');
            return lastDot > 0 ? entityClass.substring(0, lastDot) : entityClass;
        }
        return null;
    }

    public String getModuleName() {
        return module;
    }

    private String extractClassName(String fullClassName) {
        if (fullClassName != null) {
            int lastDot = fullClassName.lastIndexOf('.');
            return lastDot >= 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
        }
        return null;
    }
}
