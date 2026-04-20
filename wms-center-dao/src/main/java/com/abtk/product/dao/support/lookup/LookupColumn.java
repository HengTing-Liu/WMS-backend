package com.abtk.product.dao.support.lookup;

/**
 * Lookup 虚拟列预校验后的 JOIN 描述符。
 *
 * <p>该对象的所有字段都已通过 {@link com.abtk.product.dao.util.SqlInjectionValidator}
 * 格式校验与白名单校验，可直接作为 MyBatis {@code ${}} 占位符使用。</p>
 *
 * <p>典型 SQL 结构：</p>
 * <pre>
 * SELECT m.*, j1.warehouse_name AS warehouse_name
 * FROM inv_location m
 * LEFT JOIN sys_warehouse j1 ON m.warehouse_code = j1.warehouse_code
 *                           AND j1.is_deleted = 0
 * </pre>
 *
 * <p>放在 dao 模块，供 DynamicMapper 作为参数类型引用。</p>
 */
public class LookupColumn {

    /** JOIN 别名（j1 / j2 / ...） */
    private final String joinAlias;

    /** 关联表 tableCode（snake_case） */
    private final String refTable;

    /** 当前表外键列（snake_case） */
    private final String localField;

    /** 关联表匹配列（snake_case） */
    private final String matchField;

    /** 关联表展示列（snake_case） */
    private final String targetField;

    /** SELECT 出来的别名（= column_meta.field 的 snake_case 形式） */
    private final String aliasField;

    /** 关联表逻辑删除列（当前统一为 is_deleted） */
    private final String deleteColumn;

    /** 原始 column_meta.field（驼峰，供前端匹配） */
    private final String virtualFieldCamel;

    public LookupColumn(String joinAlias,
                        String refTable,
                        String localField,
                        String matchField,
                        String targetField,
                        String aliasField,
                        String deleteColumn,
                        String virtualFieldCamel) {
        this.joinAlias = joinAlias;
        this.refTable = refTable;
        this.localField = localField;
        this.matchField = matchField;
        this.targetField = targetField;
        this.aliasField = aliasField;
        this.deleteColumn = deleteColumn;
        this.virtualFieldCamel = virtualFieldCamel;
    }

    public String getJoinAlias() { return joinAlias; }
    public String getRefTable() { return refTable; }
    public String getLocalField() { return localField; }
    public String getMatchField() { return matchField; }
    public String getTargetField() { return targetField; }
    public String getAliasField() { return aliasField; }
    public String getDeleteColumn() { return deleteColumn; }
    public String getVirtualFieldCamel() { return virtualFieldCamel; }

    /** SQL 表达式（供 WHERE/ORDER BY 使用）：j1.warehouse_name */
    public String getSqlExpression() {
        return joinAlias + "." + targetField;
    }
}
