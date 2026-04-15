package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 仓库收货信息请求入参
 *
 * @author backend
 * @since 2026-03-18
 */
@Data
@Schema(description = "仓库收货信息请求入参")
public class WarehouseReceiverRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编码（关联inv_warehouse）
     */
    @NotBlank(message = "仓库编码不能为空")
    @Size(max = 50, message = "仓库编码长度不能超过50")
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 100, message = "收货人姓名长度不能超过100")
    @Schema(description = "收货人姓名")
    private String consignee;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @Size(max = 20, message = "手机号码长度不能超过20")
    @Schema(description = "手机号码")
    private String phoneNumber;

    /**
     * 国家
     */
    @Size(max = 50, message = "国家长度不能超过50")
    @Schema(description = "国家")
    private String country;

    /**
     * 省份
     */
    @Size(max = 50, message = "省份长度不能超过50")
    @Schema(description = "省份")
    private String province;

    /**
     * 城市
     */
    @Size(max = 50, message = "城市长度不能超过50")
    @Schema(description = "城市")
    private String city;

    /**
     * 区县
     */
    @Size(max = 50, message = "区县长度不能超过50")
    @Schema(description = "区县")
    private String district;

    /**
     * 详细地址
     */
    @NotBlank(message = "{warehouse.receiver.detailedAddress.required}")
    @Size(max = 500, message = "{validation.name.length}")
    @Schema(description = "详细地址")
    private String detailedAddress;

    /**
     * 邮政编码
     */
    @Size(max = 20, message = "邮政编码长度不能超过20")
    @Schema(description = "邮政编码")
    private String postalCode;

    /**
     * 是否默认：0-否 1-是
     */
    @NotNull(message = "是否默认不能为空")
    @Schema(description = "是否默认")
    private Integer isDefault;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500")
    @Schema(description = "备注")
    private String remarks;
}
