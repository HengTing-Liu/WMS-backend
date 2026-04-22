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
     * ERP公司名称
     */
    @Schema(description = "ERP公司名称")
    private String erpCompanyName;

    /**
     * 仓库类型
     */
    @Schema(description = "仓库类型")
    private String warehouseType;

    /**
     * 仓库所在地
     */
    @Schema(description = "仓库所在地")
    private String warehouseLocation;

    /**
     * 质量分区
     */
    @Schema(description = "质量分区")
    private String qualityZone;

    /**
     * 库位等级（存储分区/存储容器）
     */
    @Schema(description = "库位等级")
    private String locationGrade;

    /**
     * 全路径名称
     */
    @Schema(description = "全路径名称")
    private String locationFullpathName;

    /**
     * 是否已选中
     */
    @Schema(description = "是否已选中")
    private Boolean selected = false;
}
