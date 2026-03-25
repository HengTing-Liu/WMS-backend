package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 枚举定义及明细新增请求
 *
 * @author lht
 * @since 2026-03-06
 */
@Data
@Schema(description = "枚举定义及明细新增请求")
public class SysEnumDefineWithItemsRequest extends BaseRequest {

    /**
     * 枚举定义信息
     */
    @Schema(description = "枚举定义信息")
    private SysEnumDefineRequest enumDefine;

    /**
     * 枚举明细列表
     */
    @Schema(description = "枚举明细列表")
    private List<SysEnumItemRequest> enumItems;

}
