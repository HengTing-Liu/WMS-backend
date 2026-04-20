package com.abtk.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 容器配置（用于批量创建存储容器及孔位）
 */
@Data
@Schema(description = "容器配置")
public class ContainerConfig {

    /**
     * 容器类型（如：冷冻盒/抽屉/箱等）
     */
    @NotBlank(message = "容器类型不能为空")
    @Schema(description = "容器类型", required = true)
    private String locationType;

    /**
     * 容器数量
     */
    @NotNull(message = "容器数量不能为空")
    @Min(value = 1, message = "容器数量至少为1")
    @Schema(description = "容器数量", required = true)
    private Integer quantity;

    /**
     * 规格（如 4x4、8x12、96孔、48孔等）
     */
    @Schema(description = "规格")
    private String specification;

    /**
     * 孔位数量
     */
    @Min(value = 0, message = "孔位数量不能为负数")
    @Schema(description = "孔位数量")
    private Integer childrenQuantity;

    /**
     * 孔位类型
     */
    @Schema(description = "孔位类型，默认'孔'")
    private String childrenType = "孔";
}
