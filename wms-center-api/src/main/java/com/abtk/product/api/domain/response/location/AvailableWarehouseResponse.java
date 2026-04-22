package com.abtk.product.api.domain.response.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 可选仓库信息响应DTO
 *
 * @author wms
 * @since 2026-04-15
 */
@Data
@Schema(description = "可选仓库信息")
public class AvailableWarehouseResponse {

    /**
     * 仓库编码
     */
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Schema(description = "仓库名称")
    private String warehouseName;

    /**
     * 仓库类型
     */
    @Schema(description = "仓库类型")
    private String warehouseType;

    /**
     * 温区
     */
    @Schema(description = "温区")
    private String temperatureZone;

    /**
     * ERP公司名称
     */
    @Schema(description = "ERP公司名称")
    private String erpCompanyName;

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
     * 责任部门全路径
     */
    @Schema(description = "责任部门全路径")
    private String deptNameFullPath;

    /**
     * 完整显示名称
     */
    @Schema(description = "完整显示名称")
    private String displayName;
}
