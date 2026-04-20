package com.abtk.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 层级配置（用于批量创建库位层级）
 */
@Data
@Schema(description = "层级配置")
public class LevelConfig {

    /**
     * 库位类型（如：层/架/行/列/格）
     */
    @NotBlank(message = "库位类型不能为空")
    @Schema(description = "库位类型", required = true)
    private String locationType;

    /**
     * 创建数量
     */
    @NotNull(message = "创建数量不能为空")
    @Min(value = 1, message = "创建数量至少为1")
    @Schema(description = "创建数量", required = true)
    private Integer quantity;

    /**
     * 起始序号
     */
    @Min(value = 1, message = "起始序号至少为1")
    @Schema(description = "起始序号，默认1")
    private Integer startSerialNo = 1;
}
