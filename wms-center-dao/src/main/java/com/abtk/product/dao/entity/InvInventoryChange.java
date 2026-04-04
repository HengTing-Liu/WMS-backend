package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 库存变更明细表(InvInventoryChange)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class InvInventoryChange extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 库存ID
     */
    @Excel(name = "库存ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "库存ID")
    private Long inventoryId;

    /**
     * 变更类型（入库/出库/调拨/盘点调整/库位调整）
     */
    @Excel(name = "变更类型", type = Type.ALL)
    @Schema(description = "变更类型")
    private String changeType;

    /**
     * 发生日期
     */
    @Excel(name = "发生日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "发生日期")
    private Date changeDate;

    /**
     * 来源单号
     */
    @Excel(name = "来源单号", type = Type.ALL)
    @Schema(description = "来源单号")
    private String sourceNo;

    /**
     * 来源明细ID
     */
    @Excel(name = "来源明细ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "来源明细ID")
    private Long sourceDetailId;

    /**
     * 变更数量（正数入库，负数出库）
     */
    @Excel(name = "变更数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "变更数量")
    private Integer changeQuantity;

    /**
     * 变更前数量
     */
    @Excel(name = "变更前数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "变更前数量")
    private Integer quantityBefore;

    /**
     * 变更后数量
     */
    @Excel(name = "变更后数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "变更后数量")
    private Integer quantityAfter;

    /**
     * 库位ID
     */
    @Excel(name = "库位ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "库位ID")
    private Long locationId;

    /**
     * 库位排序号
     */
    @Excel(name = "库位排序号", type = Type.ALL)
    @Schema(description = "库位排序号")
    private String locationSortNo;

    /**
     * 库位路径
     */
    @Excel(name = "库位路径", type = Type.ALL)
    @Schema(description = "库位路径")
    private String locationPath;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除（0-正常，1-删除）")
    private Integer isDeleted;
}
