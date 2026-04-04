package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存出入物料明细表(IoInventoryItem)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class IoInventoryItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "台账ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "台账ID")
    private Long accountId;

    @Excel(name = "序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "序号")
    private Integer seqNo;

    @Excel(name = "物料ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "物料ID")
    private Long materialId;

    @Excel(name = "物料编码", type = Type.ALL)
    @Schema(description = "物料编码")
    private String materialCode;

    @Excel(name = "物料名称", type = Type.ALL)
    @Schema(description = "物料名称")
    private String materialName;

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

    @Excel(name = "数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "数量")
    private Integer quantity;

    @Excel(name = "是否必检", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否必检")
    private Integer isQcRequired;

    @Excel(name = "推荐盒号", type = Type.ALL)
    @Schema(description = "推荐盒号")
    private String recommendBoxNo;

    @Excel(name = "推荐孔号", type = Type.ALL)
    @Schema(description = "推荐孔号")
    private String recommendHoleNo;

    private Long locationSelectionId;

    @Excel(name = "库位全路径名称", type = Type.ALL)
    @Schema(description = "库位全路径名称")
    private String locationFullpathName;

    @Excel(name = "单价", type = Type.EXPORT)
    @Schema(description = "单价")
    private BigDecimal price;

    @Excel(name = "运保费", type = Type.EXPORT)
    @Schema(description = "运保费")
    private BigDecimal freight;

    @Excel(name = "合计金额", type = Type.EXPORT)
    @Schema(description = "合计金额")
    private BigDecimal totalAmount;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
