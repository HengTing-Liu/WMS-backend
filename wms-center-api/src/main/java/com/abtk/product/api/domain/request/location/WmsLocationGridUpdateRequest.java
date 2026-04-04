package com.abtk.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 库位网格配置更新请求
 *
 * @author wms
 * @since 2026-03-15
 */
@Data
@Schema(description = "库位网格配置更新请求")
public class WmsLocationGridUpdateRequest {

    /**
     * 库位ID
     */
    @NotNull(message = "库位ID不能为空")
    @Schema(description = "库位ID", required = true)
    private Long locationId;

    /**
     * 网格行数
     */
    @NotNull(message = "网格行数不能为空")
    @Schema(description = "网格行数", required = true)
    private Integer gridRows;

    /**
     * 网格列数
     */
    @NotNull(message = "网格列数不能为空")
    @Schema(description = "网格列数", required = true)
    private Integer gridCols;

    /**
     * 网格配置列表
     */
    @NotEmpty(message = "网格配置不能为空")
    @Schema(description = "网格位置配置列表", required = true)
    private List<GridPositionRequest> gridConfig;
}
