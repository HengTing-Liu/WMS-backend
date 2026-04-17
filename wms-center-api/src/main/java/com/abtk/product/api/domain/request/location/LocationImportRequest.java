package com.abtk.product.api.domain.request.location;

import com.abtk.product.api.domain.request.BaseRequest;
import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库位导入请求
 * 对应导入模板的字段
 *
 * @author wms
 * @since 2026-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "库位导入请求")
public class LocationImportRequest extends BaseRequest {

    /**
     * 仓库编码（必填）
     */
    @Excel(name = "仓库编码")
    @Schema(description = "仓库编码（必填）")
    private String warehouseCode;

    /**
     * 上级库位全路径（存储类型/存储分区全路径）
     */
    @Excel(name = "上级库位全路径")
    @Schema(description = "上级库位全路径（必填，为空表示根节点）")
    private String parentLocationFullpathName;

    /**
     * 库位类型（必填）
     */
    @Excel(name = "库位类型")
    @Schema(description = "库位类型（必填）：冰箱/货架/地堆/托盘/层/架/行/列/格/盒/箱/笼/抽屉/孔")
    private String locationType;

    /**
     * 库位名称（必填）
     */
    @Excel(name = "库位名称")
    @Schema(description = "库位名称（必填，最多50字符）")
    private String locationName;

    /**
     * 存储模式（选填，默认 Shared）
     * Exclusive - 独占模式
     * Shared - 共享模式
     */
    @Excel(name = "存储模式")
    @Schema(description = "存储模式（选填）：Exclusive/Shared，默认 Shared")
    private String storageMode;

    /**
     * 规格（独占模式必填）
     * 格式：4x4, 8x12 等
     */
    @Excel(name = "规格")
    @Schema(description = "规格（独占模式必填）：如 4x4, 8x12, 9x9")
    private String specification;
}
