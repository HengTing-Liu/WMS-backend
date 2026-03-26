package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 客户档案请求入参
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@Schema(description = "客户档案请求入参")
public class CustomerRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户编码
     */
    @NotBlank(message = "客户编码不能为空")
    @Size(max = 50, message = "客户编码长度不能超过50")
    @Schema(description = "客户编码")
    private String customerCode;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 100, message = "客户名称长度不能超过100")
    @Schema(description = "客户名称")
    private String customerName;

    /**
     * 联系人
     */
    @Size(max = 50, message = "联系人长度不能超过50")
    @Schema(description = "联系人")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话长度不能超过20")
    @Schema(description = "联系电话")
    private String contactPhone;

    /**
     * 手机号码
     */
    @Size(max = 20, message = "手机号码长度不能超过20")
    @Schema(description = "手机号码")
    private String mobile;

    /**
     * 邮箱
     */
    @Size(max = 100, message = "邮箱长度不能超过100")
    @Schema(description = "邮箱")
    private String email;

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
    @Size(max = 200, message = "详细地址长度不能超过200")
    @Schema(description = "详细地址")
    private String address;

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
