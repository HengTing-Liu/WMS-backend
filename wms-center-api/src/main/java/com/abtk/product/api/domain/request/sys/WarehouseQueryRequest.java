package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 仓库档案查询请求
 *
 * @author backend
 * @since 2026-03-18
 */
@Data
@Schema(description = "仓库档案查询请求")
public class WarehouseQueryRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编码
     */
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Schema(description = "仓库名称")
    private String warehouseName;

    /**
     * 所属公司
     */
    @Schema(description = "所属公司")
    private String company;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Integer isEnabled;
}
