package com.abclonal.product.api.domain.response.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 字典数据响应
 */
@Data
@Schema(description = "字典数据响应")
public class DictDataResponse {
    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    private String dictType;

    /**
     * 字典数据列表
     */
    @Schema(description = "字典数据列表")
    private java.util.List<Map<String, Object>> dataList;
}
