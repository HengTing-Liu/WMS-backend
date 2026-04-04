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

    @Excel(name = "仓库编码")
    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Excel(name = "仓库名称")
    @Schema(description = "仓库名称")
    private String warehouseName;

    @Excel(name = "温区")
    @Schema(description = "温区：COLD/ROOM/FROZEN")
    private String temperatureZone;

    @Excel(name = "仓库类型")
    @Schema(description = "仓库类型")
    private String warehouseType;

    @Excel(name = "所属公司")
    @Schema(description = "所属公司")
    private String company;

    @Excel(name = "联系人")
    @Schema(description = "联系人")
    private String contact;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话")
    private String phone;

    @Excel(name = "详细地址")
    @Schema(description = "详细地址")
    private String address;

    @Excel(name = "状态")
    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;

    @Excel(name = "创建时间")
    @Schema(description = "创建时间")
    private LocalDateTime gmtCreate;
}
