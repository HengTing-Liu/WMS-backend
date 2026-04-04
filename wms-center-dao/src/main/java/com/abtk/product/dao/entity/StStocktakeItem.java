package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存盘点明细表(StStocktakeItem)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class StStocktakeItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "盘点ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "盘点ID")
    private Long stocktakeId;

    @Excel(name = "库位计划ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "库位计划ID")
    private Long locationPlanId;

    @Excel(name = "物料ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "物料ID")
    private Long materialId;

    @Excel(name = "物料编码", type = Type.ALL)
    @Schema(description = "物料编码")
    private String materialCode;

    @Excel(name = "物料名称", type = Type.ALL)
    @Schema(description = "物料名称")
    private String materialName;

    @Excel(name = "批次ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "批次ID")
    private Long batchId;

    @Excel(name = "批次号", type = Type.ALL)
    @Schema(description = "批次号")
    private String batchNo;

    @Excel(name = "纯化编号", type = Type.ALL)
    @Schema(description = "纯化编号")
    private String purifyNo;

    @Excel(name = "克隆号", type = Type.ALL)
    @Schema(description = "克隆号")
    private String cloneNo;

    @Excel(name = "浓度", type = Type.ALL)
    @Schema(description = "浓度")
    private String concentration;

    @Excel(name = "规格", type = Type.ALL)
    @Schema(description = "规格")
    private String spec;

    @Excel(name = "计量单位", type = Type.ALL)
    @Schema(description = "计量单位")
    private String unit;

    @Excel(name = "库位ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "库位ID")
    private Long locationId;

    @Excel(name = "库位编码", type = Type.ALL)
    @Schema(description = "库位编码")
    private String locationCode;

    @Excel(name = "库位全路径名称", type = Type.ALL)
    @Schema(description = "库位全路径名称")
    private String locationFullpathName;

    @Excel(name = "系统数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "系统数量")
    private Integer systemQuantity;

    @Excel(name = "盘点数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "盘点数量")
    private Integer stocktakeQuantity;

    @Excel(name = "差异数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "差异数量")
    private Integer diffQuantity;

    @Excel(name = "盘点时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "盘点时间")
    private Date stocktakeTime;

    private Long operatorId;

    @Excel(name = "盘点人", type = Type.ALL)
    @Schema(description = "盘点人")
    private String operatorName;

    @Excel(name = "差异原因", type = Type.ALL)
    @Schema(description = "差异原因")
    private String diffReason;

    @Excel(name = "确认数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "确认数量")
    private Integer confirmQuantity;

    private Long confirmOperatorId;

    @Excel(name = "确认人", type = Type.ALL)
    @Schema(description = "确认人")
    private String confirmOperatorName;

    @Excel(name = "确认时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "确认时间")
    private Date confirmTime;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
