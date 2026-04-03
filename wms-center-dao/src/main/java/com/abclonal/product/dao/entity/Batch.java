package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 批次档案表(Batch)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class Batch extends BaseEntity {
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
     * 批次号
     */
    @Excel(name = "批次号", type = Type.ALL)
    @Schema(description = "批次号")
    private String batchNo;

    /**
     * 供应商批号
     */
    @Excel(name = "供应商批号", type = Type.ALL)
    @Schema(description = "供应商批号")
    private String supplierBatchNo;

    /**
     * 纯化编号
     */
    @Excel(name = "纯化编号", type = Type.ALL)
    @Schema(description = "纯化编号")
    private String purifyNo;

    /**
     * 克隆号
     */
    @Excel(name = "克隆号", type = Type.ALL)
    @Schema(description = "克隆号")
    private String cloneNo;

    /**
     * 浓度
     */
    @Excel(name = "浓度", type = Type.ALL)
    @Schema(description = "浓度")
    private String concentration;

    /**
     * 浓度更新时间
     */
    @Excel(name = "浓度更新时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "浓度更新时间")
    private Date concentrationUpdateTime;

    /**
     * 失效期
     */
    @Excel(name = "失效期", type = Type.ALL, dateFormat = "yyyy-MM-dd")
    @Schema(description = "失效期")
    private Date expireDate;

    /**
     * 质检数据
     */
    @Excel(name = "质检数据", type = Type.EXPORT)
    @Schema(description = "质检数据")
    private String qcData;

    /**
     * COA链接
     */
    @Excel(name = "COA链接", type = Type.EXPORT)
    @Schema(description = "COA链接")
    private String coaLink;

    /**
     * 入库日期
     */
    @Excel(name = "入库日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "入库日期")
    private Date inboundDate;

    /**
     * 生产日期
     */
    @Excel(name = "生产日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "生产日期")
    private Date productionDate;

    /**
     * 项目编号
     */
    @Excel(name = "项目编号", type = Type.ALL)
    @Schema(description = "项目编号")
    private String projectNo;

    /**
     * 裸成品物料编码
     */
    @Excel(name = "裸成品物料编码", type = Type.ALL)
    @Schema(description = "裸成品物料编码")
    private String nakedFinishCode;

    /**
     * 裸成品批次
     */
    @Excel(name = "裸成品批次", type = Type.ALL)
    @Schema(description = "裸成品批次")
    private String nakedFinishBatch;

    /**
     * 缓冲液
     */
    @Excel(name = "缓冲液", type = Type.EXPORT)
    @Schema(description = "缓冲液")
    private String bufferSolution;

    /**
     * ERP同步备注
     */
    @Excel(name = "ERP同步备注", type = Type.EXPORT)
    @Schema(description = "ERP同步备注")
    private String erpSyncRemark;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除（0-正常，1-删除）")
    private Integer isDeleted;
}
