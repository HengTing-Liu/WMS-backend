package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 库区档案查询请求
 * WMS0050 库区管理
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@Schema(description = "库区档案查询请求")
public class StorageQueryRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库区编码
     */
    @Schema(description = "库区编码")
    private String storageCode;

    /**
     * 库区名称
     */
    @Schema(description = "库区名称")
    private String storageName;

    /**
     * 仓库ID
     */
    @Schema(description = "仓库ID")
    private Long warehouseId;

    /**
     * 库位ID
     */
    @Schema(description = "库位ID")
    private Long locationId;

    /**
     * 库区类型
     */
    @Schema(description = "库区类型")
    private String storageType;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Integer isEnabled;
}
