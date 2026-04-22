package com.abtk.product.api.domain.response.sys;

import com.abtk.product.api.domain.response.BaseResponse;
import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 仓库档案响应出参（用于导出）
 *
 * @author backend
 * @since 2026-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "仓库档案响应出参")
public class WarehouseResponse extends BaseResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "仓库类型")
    @Schema(description = "仓库类型")
    private String warehouseType;

    @Excel(name = "仓库编码")
    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Excel(name = "所在地")
    @Schema(description = "所在地")
    private String warehouseLocation;

    @Excel(name = "仓库名称")
    @Schema(description = "仓库名称")
    private String warehouseName;

    @Excel(name = "温度分区")
    @Schema(description = "温度分区")
    private String temperatureZone;

    @Excel(name = "质量分区")
    @Schema(description = "质量分区")
    private String qualityZone;

    @Excel(name = "责任人工号")
    @Schema(description = "责任人工号")
    private String employeeCode;

    @Excel(name = "责任人")
    @Schema(description = "责任人")
    private String employeeName;

    @Excel(name = "责任部门编号")
    @Schema(description = "责任部门编号")
    private String deptCode;

    @Excel(name = "责任部门全路径")
    @Schema(description = "责任部门全路径")
    private String deptNameFullPath;

    @Excel(name = "ERP公司编码")
    @Schema(description = "ERP公司编码")
    private String erpCompanyCode;

    @Excel(name = "ERP公司名称")
    @Schema(description = "ERP公司名称")
    private String erpCompanyName;

    @Excel(name = "ERP仓库编码")
    @Schema(description = "ERP仓库编码")
    private String erpWarehouseCode;

    @Excel(name = "ERP货位编码")
    @Schema(description = "ERP货位编码")
    private String erpLocationCode;

    @Excel(name = "是否启用")
    @Schema(description = "是否启用：0-禁用 1-启用")
    private Integer isEnabled;

    @Excel(name = "备注")
    @Schema(description = "备注")
    private String remarks;

    @Excel(name = "存储物料")
    @Schema(description = "存储物料")
    private String storedMaterial;

    @Excel(name = "创建时间")
    @Schema(description = "创建时间")
    private LocalDateTime gmtCreate;
}