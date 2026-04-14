package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(description = "字段元数据请求对象")
public class ColumnMetaRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String tableCode;
    private String field;
    private String title;
    private String dataType;
    private String formType;
    private String dictType;
    private Integer showInList;
    private Integer showInForm;
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
}
