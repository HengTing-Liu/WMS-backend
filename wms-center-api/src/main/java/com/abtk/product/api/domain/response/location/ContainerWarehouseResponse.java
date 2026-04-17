package com.abtk.product.api.domain.response.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 存储容器仓库信息响应DTO
 *
 * @author wms
 * @since 2026-04-15
 */
@Data
@Schema(description = "存储容器仓库信息")
public class ContainerWarehouseResponse {

    /**
     * 存储容器ID
     */
    @Schema(description = "存储容器ID")
    private Long containerId;

    /**
     * 存储容器名称
     */
    @Schema(description = "存储容器名称")
    private String containerName;

    /**
     * 存储容器编号
     */
    @Schema(description = "存储容器编号")
    private String containerNo;

    /**
     * 当前仓库编码
     */
    @Schema(description = "当前仓库编码")
    private String warehouseCode;

    /**
     * 当前仓库名称
     */
    @Schema(description = "当前仓库名称")
    private String warehouseName;

    /**
     * 当前温区
     */
    @Schema(description = "当前温区")
    private String temperatureZone;

    /**
     * 是否已选中
     */
    @Schema(description = "是否已选中")
    private Boolean selected = false;
}
