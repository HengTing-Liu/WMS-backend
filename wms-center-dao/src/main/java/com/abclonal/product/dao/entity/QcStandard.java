package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 质检标准表(QcStandard)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class QcStandard extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 标准编号
     */
    @Excel(name = "标准编号", type = Type.ALL)
    @Schema(description = "标准编号")
    private String standardCode;

    /**
     * 标准名称
     */
    @Excel(name = "标准名称", type = Type.ALL)
    @Schema(description = "标准名称")
    private String standardName;

    /**
     * 公司编码
     */
    @Excel(name = "公司编码", type = Type.ALL)
    @Schema(description = "公司编码")
    private String companyCode;

    /**
     * 物料大类
     */
    @Excel(name = "物料大类", type = Type.ALL)
    @Schema(description = "物料大类")
    private String materialCategoryLarge;

    /**
     * 物料中类
     */
    @Excel(name = "物料中类", type = Type.ALL)
    @Schema(description = "物料中类")
    private String materialCategoryMiddle;

    /**
     * 物料小类
     */
    @Excel(name = "物料小类", type = Type.ALL)
    @Schema(description = "物料小类")
    private String materialCategorySmall;

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
     * 品牌
     */
    @Excel(name = "品牌", type = Type.ALL)
    @Schema(description = "品牌")
    private String brand;

    /**
     * 货号
     */
    @Excel(name = "货号", type = Type.ALL)
    @Schema(description = "货号")
    private String productNo;

    /**
     * 批次号
     */
    @Excel(name = "批次号", type = Type.ALL)
    @Schema(description = "批次号")
    private String batchNo;

    /**
     * 供应商批次号
     */
    @Excel(name = "供应商批次号", type = Type.ALL)
    @Schema(description = "供应商批次号")
    private String supplierBatchNo;

    /**
     * 质检建议（如：快递放行）
     */
    @Excel(name = "质检建议", type = Type.ALL)
    @Schema(description = "质检建议")
    private String qcSuggestion;

    /**
     * 参考等待小时数
     */
    @Excel(name = "参考等待小时数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "参考等待小时数")
    private Integer waitHours;

    /**
     * 参考质检天数
     */
    @Excel(name = "参考质检天数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "参考质检天数")
    private Integer qcDays;

    /**
     * 执行人ID
     */
    @Excel(name = "执行人ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "执行人ID")
    private Long executorId;

    /**
     * 执行人
     */
    @Excel(name = "执行人", type = Type.ALL)
    @Schema(description = "执行人")
    private String executorName;

    /**
     * 负责部门ID
     */
    @Excel(name = "负责部门ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "负责部门ID")
    private Long deptId;

    /**
     * 负责部门
     */
    @Excel(name = "负责部门", type = Type.ALL)
    @Schema(description = "负责部门")
    private String deptName;

    /**
     * 状态（0-停用，1-启用）
     */
    @Excel(name = "状态", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "状态")
    private Integer status;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
