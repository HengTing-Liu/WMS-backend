package com.abtk.product.api.domain.response.dict;

import com.abtk.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典管理响应出参
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典管理响应出参")
public class WmsDictResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 字典编码（业务标识）
     */
    @Schema(description = "字典编码（业务标识）")
    private String dictCode;

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    private String dictName;

    /**
     * 字典类型：system=系统字典，custom=自定义
     */
    @Schema(description = "字典类型：system=系统字典，custom=自定义")
    private String dictType;

    /**
     * 是否启用：0=禁用，1=启用
     */
    @Schema(description = "是否启用：0=禁用，1=启用")
    private Integer isEnabled;

    /**
     * 字典数据数量
     */
    @Schema(description = "字典数据数量")
    private Integer dataCount;
}
