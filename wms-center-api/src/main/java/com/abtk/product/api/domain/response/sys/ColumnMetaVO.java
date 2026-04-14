package com.abtk.product.api.domain.response.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "字段元数据 Schema VO（前端友好格式）")
public class ColumnMetaVO {

    @Schema(description = "字段编码（驼峰，如 warehouseCode）")
    private String code;

    @Schema(description = "字段显示名称（如 仓库编码）")
    private String label;

    @Schema(description = "数据类型（string/int/date/datetime/decimal）")
    private String type;

    @Schema(description = "表单类型（input/select/date/datetime/textarea/number）")
    private String formType;

    @Schema(description = "是否可搜索")
    private Boolean isSearchable;

    @Schema(description = "是否在列表中显示")
    private Boolean isVisible;

    @Schema(description = "是否可排序")
    private Boolean isSortable;

    @Schema(description = "是否必填")
    private Boolean isRequired;

    @Schema(description = "列宽(px)")
    private Integer width;

    @Schema(description = "排序号（升序）")
    private Integer sortOrder;

    @Schema(description = "字典类型（用于下拉框数据源）")
    private String dictType;

    @Schema(description = "数据来源（dict/api/static）")
    private String dataSource;

    @Schema(description = "API 数据来源地址")
    private String apiUrl;

    @Schema(description = "下拉选项显示字段")
    private String labelField;

    @Schema(description = "下拉选项值字段")
    private String valueField;

    @Schema(description = "组件属性 JSON（扩展用）")
    private String componentProps;

    @Schema(description = "字典下拉选项列表")
    private List<DictOptionVO> options;

    @Schema(description = "表单栅格列宽（1-24），默认6")
    private Integer colSpan;

    @Schema(description = "字段分组标识（如 basic/basicInfo/extra）")
    private String sectionKey;

    @Schema(description = "字段分组标题")
    private String sectionTitle;

    @Schema(description = "字段分组排序")
    private Integer sectionOrder;

    @Schema(description = "字段分组容器类型(card/collapse)")
    private String sectionType;

    @Schema(description = "字段分组是否默认展开: 0-否 1-是")
    private Integer sectionOpen;

    @Schema(description = "多语言 key")
    private String i18nKey;

    @Schema(description = "显示条件 JSON")
    private String visibleCondition;
}
