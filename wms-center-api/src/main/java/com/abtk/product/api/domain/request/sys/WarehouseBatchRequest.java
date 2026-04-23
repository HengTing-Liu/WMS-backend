package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 仓库档案批量创建请求入参
 * 根据温度分区和质量分区的笛卡尔积批量创建仓库
 *
 * @author backend
 * @since 2026-04-23
 */
@Data
@Schema(description = "仓库档案批量创建请求入参")
public class WarehouseBatchRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库类型
     */
    @NotBlank(message = "仓库类型不能为空")
    @Size(max = 50, message = "仓库类型长度不能超过50")
    @Schema(description = "仓库类型")
    private String warehouseType;

    /**
     * 仓库编码（自动生成，传入为空）
     */
    @Schema(description = "仓库编码（自动生成）")
    private String warehouseCode;

    /**
     * 所在地
     */
    @Size(max = 100, message = "所在地长度不能超过100")
    @Schema(description = "所在地")
    private String warehouseLocation;

    /**
     * 仓库名称
     */
    @NotBlank(message = "仓库名称不能为空")
    @Size(max = 100, message = "仓库名称长度不能超过100")
    @Schema(description = "仓库名称")
    private String warehouseName;

    /**
     * 温度分区列表（多个温度分区，与质量分区笛卡尔积）
     */
    @NotEmpty(message = "温度分区不能为空")
    @Schema(description = "温度分区列表")
    private List<String> temperatureZones;

    /**
     * 质量分区列表（多个质量分区，与温度分区笛卡尔积）
     */
    @NotEmpty(message = "质量分区不能为空")
    @Schema(description = "质量分区列表")
    private List<String> qualityZones;

    /**
     * 责任人工号
     */
    @Size(max = 50, message = "责任人工号长度不能超过50")
    @Schema(description = "责任人工号")
    private String employeeCode;

    /**
     * 责任人
     */
    @Size(max = 50, message = "责任人长度不能超过50")
    @Schema(description = "责任人")
    private String employeeName;

    /**
     * 责任部门编号
     */
    @Size(max = 50, message = "责任部门编号长度不能超过50")
    @Schema(description = "责任部门编号")
    private String deptCode;

    /**
     * 责任部门全路径
     */
    @Size(max = 200, message = "责任部门全路径长度不能超过200")
    @Schema(description = "责任部门全路径")
    private String deptNameFullPath;

    /**
     * ERP公司编码
     */
    @Size(max = 50, message = "ERP公司编码长度不能超过50")
    @Schema(description = "ERP公司编码")
    private String erpCompanyCode;

    /**
     * ERP公司名称
     */
    @Size(max = 100, message = "ERP公司名称长度不能超过100")
    @Schema(description = "ERP公司名称")
    private String erpCompanyName;

    /**
     * ERP仓库编码
     */
    @Size(max = 50, message = "ERP仓库编码长度不能超过50")
    @Schema(description = "ERP仓库编码")
    private String erpWarehouseCode;

    /**
     * ERP货位编码
     */
    @Size(max = 50, message = "ERP货位编码长度不能超过50")
    @Schema(description = "ERP货位编码")
    private String erpLocationCode;

    /**
     * 是否启用：0-禁用 1-启用
     */
    @Schema(description = "是否启用")
    private Integer isEnabled;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500")
    @Schema(description = "备注")
    private String remarks;

    /**
     * 存储物料
     */
    @Size(max = 500, message = "存储物料长度不能超过500")
    @Schema(description = "存储物料")
    private String storedMaterial;
}
