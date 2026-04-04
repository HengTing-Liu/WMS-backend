package com.abtk.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 库位档案批量请求入参
 *
 * @author wms
 * @since 2026-03-14
 */
@Data
@Schema(description = "库位档案批量请求入参")
public class WmsLocationBatchRequest {

    /**
     * 批量记录列表
     */
    @NotEmpty(message = "记录列表不能为空")
    @Size(max = 1000, message = "单次批量操作最多支持1000条记录")
    @Valid
    @Schema(description = "批量记录列表", required = true)
    private List<WmsLocationRequest> records;
}
