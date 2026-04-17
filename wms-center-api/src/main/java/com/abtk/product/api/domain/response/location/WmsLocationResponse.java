package com.abtk.product.api.domain.response.location;

import com.abtk.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库位档案表(WmsLocation)出参
 *
 * @author wms
 * @since 2026-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "库位档案响应出参")
public class WmsLocationResponse extends BaseResponse {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 上级ID
     */
    @Schema(description = "上级ID，根节点为NULL")
    private Long parentId;

    /**
     * 库位等级编码
     */
    @Schema(description = "库位等级编码")
    private String locationGrade;

    /**
     * 库位类型名称
     */
    @Schema(description = "库位类型名称")
    private String locationType;

    /**
     * 层级深度
     */
    @Schema(description = "层级深度：1=根节点，2=层，3=架...")
    private Integer locationLevel;

    /**
     * 总层数
     */
    @Schema(description = "总层数（该树的最大深度）")
    private Integer locationLevelCount;

    /**
     * 同级内部序号
     */
    @Schema(description = "同级内部序号")
    private Integer internalSerialNo;

    /**
     * 同级总数量
     */
    @Schema(description = "同级总数量")
    private Integer internalQuantity;

    /**
     * 库位名称
     */
    @Schema(description = "库位名称")
    private String locationName;

    /**
     * 仓库编码
     */
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 上级名称
     */
    @Schema(description = "上级名称")
    private String parentName;

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
     * 是否使用
     */
    @Schema(description = "是否使用：0=空闲，1=占用")
    private Integer isUse;

    /**
     * 排序号
     */
    @Schema(description = "排序号")
    private String locationSortNo;

    /**
     * 全路径名称
     */
    @Schema(description = "全路径名称")
    private String locationFullpathName;

    /**
     * 占用率（百分比）
     */
    @Schema(description = "占用率（百分比），如 75.5")
    private BigDecimal occupancyRate;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remarks;

    /**
     * 子节点列表
     */
    @Schema(description = "子节点列表")
    private List<WmsLocationResponse> children;

    /**
     * 是否有子节点（用于树形表格显示展开按钮）
     */
    @Schema(description = "是否有子节点")
    private Boolean hasChildren;
}
