package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "表单分组元数据请求对象")
public class FormGroupMetaRequest extends BaseRequest {
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
