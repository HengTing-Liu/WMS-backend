package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售提货单物料明细表(IoPickOrderSaleItem)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class IoPickOrderSaleItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "提货单ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "提货单ID")
    private Long pickId;

    private Long outboundId;
    private Long qcRecordId;

    @Excel(name = "订单号", type = Type.ALL)
    @Schema(description = "订单号")
    private String orderNo;

    private String salesUserName;

    @Excel(name = "部门全路径名称", type = Type.ALL)
    @Schema(description = "部门全路径名称")
    private String deptFullpathName;

    private Long orderDetailId;

    @Excel(name = "行号", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "行号")
    private Integer lineNo;

    @Excel(name = "物料ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "物料ID")
    private Long materialId;

    @Excel(name = "物料编码", type = Type.ALL)
    @Schema(description = "物料编码")
    private String materialCode;

    @Excel(name = "物料名称", type = Type.ALL)
    @Schema(description = "物料名称")
    private String materialName;

    @Excel(name = "出口名称", type = Type.ALL)
    @Schema(description = "出口名称")
    private String exportName;

    @Excel(name = "品牌", type = Type.ALL)
    @Schema(description = "品牌")
    private String brand;

    @Excel(name = "货号", type = Type.ALL)
    @Schema(description = "货号")
    private String productNo;

    @Excel(name = "产品类别", type = Type.ALL)
    @Schema(description = "产品类别")
    private String category;

    @Excel(name = "规格", type = Type.ALL)
    @Schema(description = "规格")
    private String spec;

    @Excel(name = "计量单位", type = Type.ALL)
    @Schema(description = "计量单位")
    private String unit;

    @Excel(name = "批次ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "批次ID")
    private Long batchId;

    @Excel(name = "批次号", type = Type.ALL)
    @Schema(description = "批次号")
    private String batchNo;

    @Excel(name = "纯化编号", type = Type.ALL)
    @Schema(description = "纯化编号")
    private String purifyNo;

    @Excel(name = "浓度", type = Type.ALL)
    @Schema(description = "浓度")
    private String concentration;

    @Excel(name = "失效期", type = Type.ALL, dateFormat = "yyyy-MM-dd")
    @Schema(description = "失效期")
    private Date expireDate;

    @Excel(name = "PoNo", type = Type.ALL)
    @Schema(description = "PoNo")
    private String poNo;

    @Excel(name = "数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "数量")
    private Integer quantity;

    @Excel(name = "单价", type = Type.EXPORT)
    @Schema(description = "单价")
    private BigDecimal price;

    @Excel(name = "运保费", type = Type.EXPORT)
    @Schema(description = "运保费")
    private BigDecimal freight;

    @Excel(name = "合计金额", type = Type.EXPORT)
    @Schema(description = "合计金额")
    private BigDecimal totalAmount;

    @Excel(name = "订单明细备注", type = Type.EXPORT)
    @Schema(description = "订单明细备注")
    private String orderRemark;

    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    private Date outConfirmTime;

    @Excel(name = "标签打印次数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "标签打印次数")
    private Integer labelPrintCount;

    @Excel(name = "说明书打印次数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "说明书打印次数")
    private Integer manualPrintCount;

    @Excel(name = "清单打印次数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "清单打印次数")
    private Integer listPrintCount;

    @Excel(name = "AC关联数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "AC关联数量")
    private Integer acLinkCount;

    @Excel(name = "出库扫码数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "出库扫码数量")
    private Integer outScanQuantity;

    @Excel(name = "出库扫码体积", type = Type.EXPORT)
    @Schema(description = "出库扫码体积")
    private BigDecimal outScanVolume;

    @Excel(name = "到货扫码数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "到货扫码数量")
    private Integer arriveScanQuantity;

    @Excel(name = "到货扫码体积", type = Type.EXPORT)
    @Schema(description = "到货扫码体积")
    private BigDecimal arriveScanVolume;

    @Excel(name = "送货扫码数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "送货扫码数量")
    private Integer deliveryScanQuantity;

    @Excel(name = "送货扫码体积", type = Type.EXPORT)
    @Schema(description = "送货扫码体积")
    private BigDecimal deliveryScanVolume;

    @Excel(name = "快递录入次数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "快递录入次数")
    private Integer expressInputCount;

    private Long expressId;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
