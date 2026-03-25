package com.abclonal.product.api.domain.response.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库位占用率统计响应
 *
 * @author wms
 * @since 2026-03-14
 */
@Data
@Schema(description = "库位占用率统计响应")
public class WmsLocationOccupancyResponse {

    /**
     * 库位ID
     */
    @Schema(description = "库位ID")
    private Long locationId;

    /**
     * 库位编号
     */
    @Schema(description = "库位编号")
    private String locationNo;

    /**
     * 库位名称
     */
    @Schema(description = "库位名称")
    private String locationName;

    /**
     * 仓库编码
     */
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 总容量
     */
    @Schema(description = "总容量")
    private Integer capacityTotal;

    /**
     * 已用容量
     */
    @Schema(description = "已用容量")
    private Integer capacityUsed;

    /**
     * 空闲容量
     */
    @Schema(description = "空闲容量")
    private Integer capacityFree;

    /**
     * 占用率（百分比）
     */
    @Schema(description = "占用率（百分比），如 75.50")
    private BigDecimal occupancyRate;

    /**
     * 存储模式
     */
    @Schema(description = "存储模式")
    private String storageMode;

    /**
     * 层级深度
     */
    @Schema(description = "层级深度")
    private Integer locationLevel;
}
