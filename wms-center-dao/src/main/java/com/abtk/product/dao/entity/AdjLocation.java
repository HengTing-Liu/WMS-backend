package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 库位调整表(AdjLocation)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class AdjLocation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 调整单号
     */
    @Excel(name = "调整单号", type = Type.ALL)
    @Schema(description = "调整单号")
    private String adjustNo;

    /**
     * 调整名称
     */
    @Excel(name = "调整名称", type = Type.ALL)
    @Schema(description = "调整名称")
    private String adjustName;

    /**
     * 申请日期
     */
    @Excel(name = "申请日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "申请日期")
    private Date applyDate;

    /**
     * 申请部门ID
     */
    @Excel(name = "申请部门ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "申请部门ID")
    private Long applyDeptId;

    /**
     * 申请部门
     */
    @Excel(name = "申请部门", type = Type.ALL)
    @Schema(description = "申请部门")
    private String applyDeptName;

    /**
     * 申请人ID
     */
    @Excel(name = "申请人ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "申请人ID")
    private Long applicantId;

    /**
     * 申请人
     */
    @Excel(name = "申请人", type = Type.ALL)
    @Schema(description = "申请人")
    private String applicantName;

    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码", type = Type.ALL)
    @Schema(description = "仓库编码")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Excel(name = "仓库名称", type = Type.ALL)
    @Schema(description = "仓库名称")
    private String warehouseName;

    /**
     * 调出库存ID
     */
    @Excel(name = "调出库存ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "调出库存ID")
    private Long outInventoryId;

    /**
     * 调出库位ID
     */
    @Excel(name = "调出库位ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "调出库位ID")
    private Long outLocationId;

    /**
     * 调出库位全路径
     */
    @Excel(name = "调出库位全路径", type = Type.ALL)
    @Schema(description = "调出库位全路径")
    private String outLocationFullpath;

    /**
     * 调入库位ID
     */
    @Excel(name = "调入库位ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "调入库位ID")
    private Long inLocationId;

    /**
     * 调入库位全路径
     */
    @Excel(name = "调入库位全路径", type = Type.ALL)
    @Schema(description = "调入库位全路径")
    private String inLocationFullpath;

    /**
     * 物料ID
     */
    @Excel(name = "物料ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", type = Type.ALL)
    @Schema(description = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", type = Type.ALL)
    @Schema(description = "物料名称")
    private String materialName;

    /**
     * 批次ID
     */
    @Excel(name = "批次ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "批次ID")
    private Long batchId;

    /**
     * 批次号
     */
    @Excel(name = "批次号", type = Type.ALL)
    @Schema(description = "批次号")
    private String batchNo;

    /**
     * 调整数量
     */
    @Excel(name = "调整数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "调整数量")
    private Integer adjustQuantity;

    /**
     * 状态
     */
    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    /**
     * 完工日期
     */
    @Excel(name = "完工日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "完工日期")
    private Date finishDate;

    /**
     * 完工操作员ID
     */
    @Excel(name = "完工操作员ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "完工操作员ID")
    private Long finishOperatorId;

    /**
     * 完工操作员
     */
    @Excel(name = "完工操作员", type = Type.ALL)
    @Schema(description = "完工操作员")
    private String finishOperatorName;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
