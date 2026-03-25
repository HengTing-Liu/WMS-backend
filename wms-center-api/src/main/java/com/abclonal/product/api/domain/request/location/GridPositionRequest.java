package com.abclonal.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 网格位置配置项
 */
@Data
@Schema(description = "网格位置配置")
public class GridPositionRequest {
    
    @NotNull(message = "行号不能为空")
    @Min(value = 1, message = "行号从1开始")
    @Schema(description = "行号，从1开始", required = true)
    private Integer row;
    
    @NotNull(message = "列号不能为空")
    @Min(value = 1, message = "列号从1开始")
    @Schema(description = "列号，从1开始", required = true)
    private Integer col;
    
    @Schema(description = "是否已占用", defaultValue = "false")
    private Boolean occupied = false;
    
    @Schema(description = "样品ID")
    private String sampleId;
    
    @Schema(description = "样品编码")
    private String sampleCode;
}
