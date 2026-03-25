package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 库位档案表(WmsLocation)实体类
 * 支持递归树形结构 + 独占/共享模式
 *
 * @author wms
 * @since 2026-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WmsLocation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 上级ID，根节点为NULL
     */
    @Excel(name = "上级ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "上级ID，根节点为NULL")
    private Long parentId;

    /**
     * 库位等级编码
     */
    @Excel(name = "库位等级编码", type = Type.ALL)
    @Schema(description = "库位等级编码")
    private String locationGrade;

    /**
     * 库位类型名称
     */
    @Excel(name = "库位类型", type = Type.ALL)
    @Schema(description = "库位类型名称（如：冰箱/层/架/行/盒/箱/孔等）")
    private String locationType;

    /**
     * 层级深度：1=根节点，2=层，3=架...
     */
    @Excel(name = "层级深度", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "层级深度：1=根节点，2=层，3=架...")
    private Integer locationLevel;

    /**
     * 总层数（该树的最大深度）
     */
    @Excel(name = "总层数", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "总层数（该树的最大深度）")
    private Integer locationLevelCount;

    /**
     * 同级内部序号
     */
    @Excel(name = "同级序号", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "同级内部序号（如第1层、第2层）")
    private Integer internalSerialNo;

    /**
     * 同级总数量
     */
    @Excel(name = "同级总数", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "同级总数量（如该层共有2个）")
    private Integer internalQuantity;

    /**
     * 库位编号（业务编码）
     */
    @Excel(name = "库位编号", type = Type.ALL)
    @Schema(description = "库位编号（业务编码）")
    private String locationNo;

    /**
     * 库位名称
     */
    @Excel(name = "库位名称", type = Type.ALL)
    @Schema(description = "库位名称")
    private String locationName;

    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码", type = Type.ALL)
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 上级名称（冗余，方便查询）
     */
    @Excel(name = "上级名称", type = Type.ALL)
    @Schema(description = "上级名称（冗余，方便查询）")
    private String parentName;

    /**
     * 存储模式：Exclusive(独占模式)/Shared(共享模式)
     */
    @Excel(name = "存储模式", type = Type.ALL)
    @Schema(description = "存储模式：Exclusive(独占模式)/Shared(共享模式)")
    private String storageMode;

    /**
     * 规格：如 4x4、1x1、96孔、48孔等
     */
    @Excel(name = "规格", type = Type.ALL)
    @Schema(description = "规格：如 4x4、1x1、96孔、48孔等")
    private String specification;

    /**
     * 网格配置，如 "4x4", "8x12"
     */
    @Excel(name = "网格配置", type = Type.ALL)
    @Schema(description = "网格配置，如 \"4x4\", \"8x12\"")
    private String gridConfig;

    /**
     * 是否使用：0=空闲，1=占用
     */
    @Excel(name = "是否使用", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否使用：0=空闲，1=占用（仅对孔位/独占模式有效）")
    private Integer isUse;

    /**
     * 排序号（用于树形展示排序）
     */
    @Excel(name = "排序号", type = Type.ALL)
    @Schema(description = "排序号（用于树形展示排序，如 L0001001001）")
    private String locationSortNo;

    /**
     * 全路径名称
     */
    @Excel(name = "全路径名称", type = Type.ALL)
    @Schema(description = "全路径名称（如：卧式冰箱_层1/2_架1/2_行1/2_BOX001_A01）")
    private String locationFullpathName;

    /**
     * 总容量（子节点数量或孔位数）
     */
    @Excel(name = "总容量", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "总容量（子节点数量或孔位数）")
    private Integer capacityTotal;

    /**
     * 已用容量（仅独占模式计算）
     */
    @Excel(name = "已用容量", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "已用容量（仅独占模式计算）")
    private Integer capacityUsed;

    /**
     * 是否删除：0=正常，1=已删除
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除：0=正常，1=已删除")
    private Integer isDeleted;

    /**
     * 备注
     */
    @Excel(name = "备注", type = Type.ALL)
    @Schema(description = "备注")
    private String remarks;

    /**
     * 子节点列表（非数据库字段，由业务层填充）
     */
    @Schema(description = "子节点列表")
    private List<WmsLocation> children;
}
