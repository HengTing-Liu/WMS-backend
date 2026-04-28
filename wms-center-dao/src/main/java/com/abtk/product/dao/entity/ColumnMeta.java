package com.abtk.product.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段元数据实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ColumnMeta extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 表标识 */
    private String tableCode;

    /** 字段名(英文) */
    private String field;

    /** 显示名称(中文) */
    private String title;

    /** 数据类型: string/int/bigint/decimal/date/datetime/text/boolean */
    private String dataType;

    /** 表单类型: input/select/date/textarea/switch */
    private String formType;

    /** 字典类型 */
    private String dictType;

    /** 联动配置JSON */
    private String linkageJson;

    /** 列表显示: 0-否 1-是 */
    private Integer showInList;

    /** 表单显示: 0-否 1-是 */
    private Integer showInForm;

    /** 可搜索: 0-否 1-是 */
    private Integer searchable;

    /** 可排序: 0-否 1-是 */
    private Integer sortable;

    /** 是否唯一: 0-否 1-是 */
    private Integer unique;

    /** 必填: 0-否 1-是 */
    private Integer required;

    /** 列宽(px) */
    private Integer width;

    /** 排序号 */
    private Integer sortOrder;

    /** 校验规则JSON */
    private String rulesJson;

    /** 占位提示 */
    private String placeholder;

    /** 默认值 */
    private String defaultValue;

    /** 状态: 0-禁用 1-启用 */
    private Integer status;

    /** 备注 */
    private String remarks;

    /** 数据库列名（原始） */
    private String columnName;

    /** 数据库列类型 */
    private String columnType;

    /** 是否主键 */
    private Boolean primaryKey;

    /** 是否可为空 */
    private Boolean nullable;

    /** 列大小/精度 */
    private Integer columnSize;

    /** 小数位数 */
    private Integer decimalDigits;

    /** 组件属性(JSON) */
    private String componentProps;

    /** 数据来源: dict/api/static */
    private String dataSource;

    /** API地址 */
    private String apiUrl;

    /** 显示字段 */
    private String labelField;

    /** 值字段 */
    private String valueField;

    /** 表单栅格列宽（1-24），默认6 */
    private Integer colSpan;

    /** 是否只读: 0-否 1-是 */
    private Integer readonly;

    /** 编辑时是否只读: 0-否 1-是 */
    private Integer editReadonly;

    /** 字段分组标识 */
    private String sectionKey;

    /** 字段分组标题 */
    private String sectionTitle;

    /** 字段分组排序 */
    private Integer sectionOrder;

    /** 字段分组容器类型 */
    private String sectionType;

    /** 字段分组是否默认展开: 0-否 1-是 */
    private Integer sectionOpen;

    /** 多语言 key */
    private String i18nKey;

    /** 显示条件 JSON */
    private String visibleCondition;

    /** 导出显示: 0-否 1-是 */
    private Integer showInExport;

    /** 导入显示: 0-否 1-是 */
    private Integer showInImport;

    // ========== Lookup 虚拟列配置（关联表字段显示） ==========
    // 当 refTableCode + refMatchField + refTargetField 都不为空时，此字段视为“虚拟联表列”。
    // 运行时由 LookupSqlBuilder 转为 LEFT JOIN 并将目标字段以 field 作为别名返回。
    // 数据库无需存在同名物理列。

    /** 关联表 tableCode（必须存在于 sys_table_meta） */
    private String refTableCode;

    /** 关联表匹配字段(snake_case) */
    private String refMatchField;

    /** 关联表展示字段(snake_case) */
    private String refTargetField;

    /** 当前表外键字段(snake_case)，为空时默认取 field 自身 */
    private String refLocalField;

    /**
     * 虚拟列多字段拼接分隔符（WMS-LOWCODE-LOOKUP-SEP）
     * <p>仅当 refTargetField 含多字段（逗号分隔）时生效；NULL 或空串时运行时兜底为 ❤。
     * 长度限制 1–4 字符，由 service 层 LookupSqlBuilder 校验，传值使用 MyBatis 参数化防止注入。</p>
     */
    private String refSeparator;

    /** 关联流水号规则名称（对应 sys_serial_number.name） */
    private String serialNumberRule;

    // Lookup 字段的 setter（显式声明，避免 MyBatis 反射问题）
    public void setRefTableCode(String refTableCode) {
        this.refTableCode = refTableCode;
    }

    public void setRefMatchField(String refMatchField) {
        this.refMatchField = refMatchField;
    }

    public void setRefTargetField(String refTargetField) {
        this.refTargetField = refTargetField;
    }

    public void setRefLocalField(String refLocalField) {
        this.refLocalField = refLocalField;
    }

    // 代码生成器适配方法（兼容 common.generator.ColumnMeta）
    public String getJavaType() {
        return mapDataTypeToJavaType(dataType);
    }

    public String getJavaFieldName() {
        return field;
    }

    public String getColumnComment() {
        return title;
    }

    public Integer getIsShow() {
        return showInList;
    }

    public Integer getIsEdit() {
        return showInForm;
    }

    public Integer getIsRequired() {
        return required;
    }

    private String mapDataTypeToJavaType(String dataType) {
        if (dataType == null) return "String";
        switch (dataType) {
            case "int": return "Integer";
            case "bigint": return "Long";
            case "decimal": return "BigDecimal";
            case "date": return "Date";
            case "datetime": return "Date";
            case "text": return "String";
            case "boolean": return "Boolean";
            default: return "String";
        }
    }
}
