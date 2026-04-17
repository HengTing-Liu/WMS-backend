package com.abtk.product.api.domain.response.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 库位绑定状态查询响应
 *
 * @author wms
 * @since 2026-04-15
 */
@Data
@Schema(description = "库位绑定状态查询响应")
public class LocationBindStatusResponse {

    /**
     * 是否已绑定物料
     */
    @Schema(description = "是否已绑定物料：true=已绑定，false=未绑定")
    private Boolean isBound = false;

    /**
     * 绑定的物料类型（如有）
     */
    @Schema(description = "绑定的物料类型")
    private String boundType;

    /**
     * 绑定的物料名称（如有）
     */
    @Schema(description = "绑定的物料名称")
    private String boundMaterialName;
}
