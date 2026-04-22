package com.abtk.product.api.domain.response.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分配仓库初始化响应DTO
 *
 * @author wms
 * @since 2026-04-15
 */
@Data
@Schema(description = "分配仓库初始化响应")
public class AssignWarehouseInitResponse {

    /**
     * 原仓库编码
     */
    @Schema(description = "原仓库编码")
    private String originalWarehouseCode;

    /**
     * 原仓库名称
     */
    @Schema(description = "原仓库名称")
    private String originalWarehouseName;

    /**
     * 原仓库温区
     */
    @Schema(description = "原仓库温区")
    private String originalTemperatureZone;

    /**
     * 原仓库类型
     */
    @Schema(description = "原仓库类型")
    private String originalWarehouseType;

    /**
     * 原仓库ERP公司名称
     */
    @Schema(description = "原仓库ERP公司名称")
    private String originalErpCompanyName;

    /**
     * 原仓库所在地
     */
    @Schema(description = "原仓库所在地")
    private String originalWarehouseLocation;

    /**
     * 原仓库质量分区
     */
    @Schema(description = "原仓库质量分区")
    private String originalQualityZone;

    /**
     * 原仓库责任人
     */
    @Schema(description = "原仓库责任人")
    private String originalEmployeeName;

    /**
     * 原仓库存储物料
     */
    @Schema(description = "原仓库存储物料")
    private String originalStoredMaterial;

    /**
     * 原仓库责任部门全路径
     */
    @Schema(description = "原仓库责任部门全路径")
    private String originalDeptNameFullPath;

    /**
     * 存储容器列表
     */
    @Schema(description = "存储容器列表")
    private List<ContainerWarehouseResponse> containers;

    /**
     * 可选仓库列表（温区一致的隔离仓/留样仓）
     */
    @Schema(description = "可选仓库列表")
    private List<AvailableWarehouseResponse> availableWarehouses;
}
