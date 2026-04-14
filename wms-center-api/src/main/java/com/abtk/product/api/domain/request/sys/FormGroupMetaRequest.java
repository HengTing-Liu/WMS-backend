package com.abtk.product.api.domain.request.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "表单分组元数据请求对象")
public class FormGroupMetaRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String tableCode;
    private String groupCode;
    private String groupTitle;
    private String groupType;
    private Integer sortOrder;
    private Integer defaultOpen;
    private Integer status;
    private String remarks;
}
