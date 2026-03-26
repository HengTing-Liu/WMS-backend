package com.abclonal.product.api.domain.request.dict;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典管理请求入参
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典管理请求入参")
public class WmsDictRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 字典编码（业务标识）
     */
    @NotBlank(message = "字典编码不能为空")
    @Size(max = 100, message = "字典编码长度不能超过100个字符")
    @Schema(description = "字典编码（业务标识）")
    private String dictCode;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称长度不能超过100个字符")
    @Schema(description = "字典名称")
    private String dictName;

    /**
     * 字典类型：system=系统字典，custom=自定义
     */
    @Size(max = 20, message = "字典类型长度不能超过20个字符")
    @Schema(description = "字典类型：system=系统字典，custom=自定义")
    private String dictType;

    /**
     * 是否启用：0=禁用，1=启用
     */
    @Schema(description = "是否启用：0=禁用，1=启用")
    private Integer isEnabled;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注")
    private String remark;
}
