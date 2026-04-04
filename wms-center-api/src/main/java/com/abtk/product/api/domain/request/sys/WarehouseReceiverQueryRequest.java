package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 仓库收货信息查询请求
 *
 * @author backend
 * @since 2026-03-18
 */
@Data
@Schema(description = "仓库收货信息查询请求")
public class WarehouseReceiverQueryRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编码
     */
    @Schema(description = "仓库编码")
    private String warehouseCode;
}
