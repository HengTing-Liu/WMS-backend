package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 质量记录表(QcRecord)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class QcRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 检验单号
     */
    @Excel(name = "检验单号", type = Type.ALL)
    @Schema(description = "检验单号")
    private String recordNo;

    /**
     * 申请日期
     */
    @Excel(name = "申请日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "申请日期")
    private Date applyDate;

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
     * 来源单据类型
     */
    @Excel(name = "来源单据类型", type = Type.ALL)
    @Schema(description = "来源单据类型")
    private String sourceType;

    /**
     * 来源单据号
     */
    @Excel(name = "来源单据号", type = Type.ALL)
    @Schema(description = "来源单据号")
    private String sourceNo;

    /**
     * 来源单据明细行ID
     */
    @Excel(name = "来源单据明细行ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "来源单据明细行ID")
    private Long sourceDetailId;

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
     * 纯化编号
     */
    @Excel(name = "纯化编号", type = Type.ALL)
    @Schema(description = "纯化编号")
    private String purifyNo;

    /**
     * 数量
     */
    @Excel(name = "数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "数量")
    private Integer quantity;

    /**
     * 质检标准ID
     */
    @Excel(name = "质检标准ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "质检标准ID")
    private Long qcStandardId;

    /**
     * 检验执行人ID
     */
    @Excel(name = "检验执行人ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "检验执行人ID")
    private Long qcExecutorId;

    /**
     * 检验执行人
     */
    @Excel(name = "检验执行人", type = Type.ALL)
    @Schema(description = "检验执行人")
    private String qcExecutorName;

    /**
     * 检验部门ID
     */
    @Excel(name = "检验部门ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "检验部门ID")
    private Long qcDeptId;

    /**
     * 检验部门
     */
    @Excel(name = "检验部门", type = Type.ALL)
    @Schema(description = "检验部门")
    private String qcDeptName;

    /**
     * 状态（未开始/质检中/已完成）
     */
    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    /**
     * 是否有COA（0-否，1-是）
     */
    @Excel(name = "是否有COA", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否有COA")
    private Integer hasCoa;

    /**
     * 检验组别
     */
    @Excel(name = "检验组别", type = Type.ALL)
    @Schema(description = "检验组别")
    private String qcGroup;

    /**
     * 质检损耗量
     */
    @Excel(name = "质检损耗量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "质检损耗量")
    private Integer qcLossQuantity;

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
     * 其他特殊情况备注
     */
    @Excel(name = "其他特殊情况备注", type = Type.EXPORT)
    @Schema(description = "其他特殊情况备注")
    private String specialRemark;

    /**
     * 放行结论（合格/不合格）
     */
    @Excel(name = "放行结论", type = Type.ALL)
    @Schema(description = "放行结论")
    private String releaseConclusion;

    /**
     * 逾期天数
     */
    @Excel(name = "逾期天数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "逾期天数")
    private Integer overdueDays;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
