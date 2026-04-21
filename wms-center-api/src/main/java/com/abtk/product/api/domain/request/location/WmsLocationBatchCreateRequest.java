package com.abtk.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

/**
 * 批量创建库位请求
 * 用于根据模板批量生成库位结构（如冰箱-层-架-行-盒-孔）
 *
 * @author wms
 * @since 2026-03-14
 */
@Data
@Schema(description = "批量创建库位请求")
public class WmsLocationBatchCreateRequest {

    /**
     * 父级ID
     */
    @NotNull(message = "父级ID不能为空")
    @Schema(description = "父级ID，根节点可为空", required = true)
    private Long parentId;

    /**
     * 仓库编码
     */
    @NotBlank(message = "仓库编码不能为空")
    @Schema(description = "仓库编码", required = true)
    private String warehouseCode;

    /**
     * 库位等级编码
     */
    @NotBlank(message = "库位等级编码不能为空")
    @Schema(description = "库位等级编码", required = true)
    private String locationGrade;

    /**
     * 库位类型
     */
    @NotBlank(message = "库位类型不能为空")
    @Schema(description = "库位类型（如：冰箱/层/架/行/盒/孔等）", required = true)
    private String locationType;

    /**
     * 创建数量
     */
    @NotNull(message = "创建数量不能为空")
    @Min(value = 1, message = "创建数量至少为1")
    @Max(value = 1000, message = "单次最多创建1000个")
    @Schema(description = "创建数量", required = true)
    private Integer quantity;

    /**
     * 库位名称前缀
     */
    @Schema(description = "库位名称前缀")
    private String locationNamePrefix;

    /**
     * 库位名称（用于指定流水号规则名称）
     */
    @Schema(description = "库位名称（用于指定流水号规则名称）")
    private String locationName;

    /**
     * 起始序号
     */
    @Min(value = 1, message = "起始序号至少为1")
    @Schema(description = "起始序号，默认1")
    private Integer startSerialNo = 1;

    /**
     * 存储模式
     */
    @Schema(description = "存储模式：Exclusive(独占模式)/Shared(共享模式)")
    private String storageMode;

    /**
     * 规格
     */
    @Schema(description = "规格：如 4x4、1x1、96孔、48孔等")
    private String specification;

    /**
     * 是否同时创建子节点（适用于盒创建孔）
     */
    @Schema(description = "是否同时创建子节点（适用于盒创建孔）")
    private Boolean createChildren = false;

    /**
     * 子节点数量（如16孔、96孔）
     */
    @Min(value = 1, message = "子节点数量至少为1")
    @Schema(description = "子节点数量（如16孔、96孔）")
    private Integer childrenQuantity;

    /**
     * 子节点类型
     */
    @Schema(description = "子节点类型")
    private String childrenType;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remarks;
}
