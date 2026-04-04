package com.abtk.product.api.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 17:18
 *
 */
@Data
@Schema(description = "下拉选项")
public class OptionVO {
    @Schema(description = "选项值（ID）", example = "1")
    private String value;   // 或用 id，但前端常用 value

    @Schema(description = "选项标签（名称）", example = "管理员")
    private String label;
}