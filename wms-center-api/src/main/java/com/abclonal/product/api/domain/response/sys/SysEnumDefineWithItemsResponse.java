package com.abclonal.product.api.domain.response.sys;

import com.abclonal.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 枚举定义及明细新增响应
 *
 * @author lht
 * @since 2026-03-06
 */
@Data
@Schema(description = "枚举定义及明细新增响应")
public class SysEnumDefineWithItemsResponse extends BaseResponse {

    /**
     * 枚举定义ID
     */
    @Schema(description = "枚举定义ID")
    private Long enumDefineId;

    /**
     * 枚举编码
     */
    @Schema(description = "枚举编码")
    private String enumCode;

    /**
     * 枚举名称
     */
    @Schema(description = "枚举名称")
    private String enumName;

    /**
     * 枚举明细列表
     */
    @Schema(description = "枚举明细列表")
    private List<SysEnumItemResponse> enumItems;

}
