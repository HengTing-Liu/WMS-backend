package com.abtk.product.api.domain.request.location;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库位树形结构查询请求
 *
 * @author wms
 * @since 2026-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "库位树形结构查询请求")
public class WmsLocationTreeRequest extends BaseRequest {

    /**
     * 根节点ID，为空则查询所有根节点
     */
    @Schema(description = "根节点ID，为空则查询所有根节点")
    private Long rootId;

    /**
     * 仓库编码
     */
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 库位等级编码
     */
    @Schema(description = "库位等级编码")
    private String locationGrade;

    /**
     * 库位类型
     */
    @Schema(description = "库位类型")
    private String locationType;

    /**
     * 存储模式
     */
    @Schema(description = "存储模式：Exclusive(独占模式)/Shared(共享模式)")
    private String storageMode;

    /**
     * 是否查询已占用
     */
    @Schema(description = "是否使用：0=空闲，1=占用")
    private Integer isUse;

    /**
     * 是否递归查询所有子节点
     */
    @Schema(description = "是否递归查询所有子节点，默认true")
    private Boolean recursive = true;

    /**
     * 最大查询层级，为空则不限制
     */
    @Schema(description = "最大查询层级，为空则不限制")
    private Integer maxLevel;
}
