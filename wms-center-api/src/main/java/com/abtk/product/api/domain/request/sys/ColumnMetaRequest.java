package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "字段元数据请求对象")
public class ColumnMetaRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private List<Long> ids;
    private String tableCode;
    private String field;
    private String title;
    private String dataType;
    private String formType;
    private String dictType;
    private Integer showInList;
    private Integer showInForm;
    private Integer showInExport;
    private Integer showInImport;
    private Integer searchable;
    private Integer sortable;
    private Integer required;
    private Integer width;
    private Integer sortOrder;
    private String rulesJson;
    private String placeholder;
    private String defaultValue;
    private Integer status;
    private String componentProps;
    private String dataSource;
    private String apiUrl;
    private String labelField;
    private String valueField;
    private Integer colSpan;
    private Integer readonly;
    private Integer editReadonly;
    private String sectionKey;
    private String sectionTitle;
    private Integer sectionOrder;
    private String sectionType;
    private Integer sectionOpen;
    private String i18nKey;
    private String visibleCondition;
    // 补全缺失字段
    private String linkageJson;
    private String remarks;
    private String columnName;
    private String columnType;
    private Boolean primaryKey;
    private Boolean nullable;
    private Integer columnSize;
    private Integer decimalDigits;

    // ========== Lookup 虚拟列配置（WMS-LOWCODE-LOOKUP） ==========
    @Schema(description = "关联表 tableCode（虚拟联表列用）")
    private String refTableCode;
    @Schema(description = "关联表匹配字段(snake_case)")
    private String refMatchField;
    @Schema(description = "关联表展示字段(snake_case)")
    private String refTargetField;
    @Schema(description = "当前表外键字段(snake_case)，为空时默认取 field 自身")
    private String refLocalField;
    @Schema(description = "虚拟列多字段拼接分隔符，仅多字段场景生效，空值时后端兜底为 ❤，长度 1-4")
    private String refSeparator;

    @Schema(description = "关联流水号规则编码（对应 sys_serial_number.usage_scope）")
    private String serialNumberRule;
}
