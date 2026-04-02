package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 仓库档案请求入参
 *
 * @author backend
 * @since 2026-03-18
 */
@Data
@Schema(description = "仓库档案请求入参")
public class WarehouseRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编码
     */
    @NotBlank(message = "仓库编码不能为空")
    @Size(max = 50, message = "仓库编码长度不能超过50")
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @NotBlank(message = "仓库名称不能为空")
    @Size(max = 100, message = "仓库名称长度不能超过100")
    @Schema(description = "仓库名称")
    private String warehouseName;

    /**
     * 温度分区
     */
    @Size(max = 50, message = "温度分区长度不能超过50")
    @Schema(description = "温度分区")
    private String temperatureZone;

    /**
     * 质量分区
     */
    @Size(max = 50, message = "质量分区长度不能超过50")
    @Schema(description = "质量分区")
    private String qualityZone;

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
     * 所属公司
     */
    @NotBlank(message = "所属公司不能为空")
    @Size(max = 100, message = "所属公司长度不能超过100")
    @Schema(description = "所属公司")
    private String company;

    /**
     * 是否启用：0-禁用 1-启用
     */
    @NotNull(message = "是否启用不能为空")
    @Schema(description = "是否启用")
    private Integer isEnabled;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500")
    @Schema(description = "备注")
    private String remark;
}
