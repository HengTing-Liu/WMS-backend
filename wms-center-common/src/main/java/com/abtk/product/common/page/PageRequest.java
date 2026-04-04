package com.abtk.product.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PageRequest {
    @Schema(description = "当前页码，从 1 开始", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页显示条数", example = "10")
    private Integer pageSize = 10;
}
