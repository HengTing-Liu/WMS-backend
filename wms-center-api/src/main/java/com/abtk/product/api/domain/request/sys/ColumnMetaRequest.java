package com.abtk.product.api.domain.request.sys;

<<<<<<< HEAD:wms-center-api/src/main/java/com/abclonal/product/api/domain/request/sys/ColumnMetaRequest.java
=======
import com.abtk.product.api.domain.request.BaseRequest;
>>>>>>> 5e4b635:wms-center-api/src/main/java/com/abtk/product/api/domain/request/sys/ColumnMetaRequest.java
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
    private String i18nKey;
    private String visibleCondition;
}
