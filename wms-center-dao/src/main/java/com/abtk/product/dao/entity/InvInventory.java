package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 库存余量表(InvInventory)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class InvInventory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

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
     * 仓库编码
     */
    @Excel(name = "仓库编码", type = Type.ALL)
    @Schema(description = "仓库编码")
    private String warehouseCode;

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
     * 库位全路径名称
     */
    @Excel(name = "库位全路径名称", type = Type.ALL)
    @Schema(description = "库位全路径名称")
    private String locationFullpathName;

    /**
     * 数量
     */
    @Excel(name = "数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "数量")
    private Integer quantity;

    /**
     * 剩余量
     */
    @Excel(name = "剩余量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "剩余量")
    private Integer remainingQuantity;

    /**
     * 出库在途量
     */
    @Excel(name = "出库在途量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "出库在途量")
    private Integer inTransitQuantity;

    /**
     * 冻结数量
     */
    @Excel(name = "冻结数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "冻结数量")
    private Integer freezeQuantity;

    /**
     * 状态（待检验/检验中/正常/盘库中/库位调整中）
     */
    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态（待检验/检验中/正常/盘库中/库位调整中）")
    private String status;

    /**
     * 质检状态
     */
    @Excel(name = "质检状态", type = Type.ALL)
    @Schema(description = "质检状态")
    private String qcStatus;

    /**
     * 是否生成二维码（0-否，1-是）
     */
    @Excel(name = "是否生成二维码", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否生成二维码（0-否，1-是）")
    private Integer isGenerateQrcode;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除（0-正常，1-删除）")
    private Integer isDeleted;
}
