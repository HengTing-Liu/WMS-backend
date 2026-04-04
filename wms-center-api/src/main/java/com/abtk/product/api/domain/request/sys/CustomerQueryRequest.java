package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户档案查询请求
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@Schema(description = "客户档案查询请求")
public class CustomerQueryRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户编码
     */
    @Schema(description = "客户编码")
    private String customerCode;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称")
    private String customerName;

    /**
     * 联系人
     */
    @Schema(description = "联系人")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactPhone;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    private String mobile;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Integer isEnabled;
}
