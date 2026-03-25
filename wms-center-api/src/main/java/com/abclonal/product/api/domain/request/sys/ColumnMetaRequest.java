package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 列元数据保存请求
 */
@Data
@Schema(description = "列元数据保存请求")
public class ColumnMetaRequest extends BaseRequest {

    @Schema(description = "主键ID(新增时为空)")
    private Long id;

    @NotBlank(message = "表标识不能为空")
    @Schema(description = "表标识", required = true)
    private String tableCode;

    @NotBlank(message = "字段名不能为空")
    @Schema(description = "字段名(驼峰)", required = true)
    private String field;

    @NotBlank(message = "显示名称不能为空")
    @Schema(description = "显示名称", required = true)
    private String title;

    @Schema(description = "数据类型(string/int/date/datetime/decimal)")
    private String dataType;

    @Schema(description = "表单类型(input/select/date/datetime/textarea/number)")
    private String formType;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "列表显示")
    private Boolean isShowInList = true;

    @Schema(description = "表单显示")
    private Boolean isShowInForm = true;

    @Schema(description = "可搜索")
    private Boolean isSearchable = false;

    @Schema(description = "可排序")
    private Boolean isSortable = false;

    @Schema(description = "必填")
    private Boolean isRequired = false;

    @Schema(description = "列宽(px)")
    private Integer width;

    @Schema(description = "排序号")
    private Integer sortOrder = 0;

    @Schema(description = "校验规则JSON")
    private String rulesJson;

    @Schema(description = "状态(0-禁用,1-启用)")
    private Integer status = 1;

    @Schema(description = "组件属性(JSON)")
    private String componentProps;

    @Schema(description = "数据来源(dict/api/static)")
    private String dataSource;

    @Schema(description = "API地址")
    private String apiUrl;

    @Schema(description = "显示字段")
    private String labelField;

    @Schema(description = "值字段")
    private String valueField;
}
