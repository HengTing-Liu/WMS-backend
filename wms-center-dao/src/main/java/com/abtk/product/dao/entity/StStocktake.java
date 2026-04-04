package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 库存盘点主表(StStocktake)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class StStocktake extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 盘点单号
     */
    @Excel(name = "盘点单号", type = Type.ALL)
    @Schema(description = "盘点单号")
    private String stocktakeNo;

    /**
     * 盘点名称
     */
    @Excel(name = "盘点名称", type = Type.ALL)
    @Schema(description = "盘点名称")
    private String stocktakeName;

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
     * 公司编码
     */
    @Excel(name = "公司编码", type = Type.ALL)
    @Schema(description = "公司编码")
    private String companyCode;

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
     * 盘点类型（全盘/抽盘/周期盘）
     */
    @Excel(name = "盘点类型", type = Type.ALL)
    @Schema(description = "盘点类型")
    private String stocktakeType;

    /**
     * 盘点范围（全仓库/指定库区/指定库位）
     */
    @Excel(name = "盘点范围", type = Type.ALL)
    @Schema(description = "盘点范围")
    private String stocktakeScope;

    /**
     * 审批时间
     */
    @Excel(name = "审批时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审批时间")
    private Date approvalTime;

    /**
     * 审批人ID
     */
    @Excel(name = "审批人ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "审批人ID")
    private Long approvalUserId;

    /**
     * 审批人
     */
    @Excel(name = "审批人", type = Type.ALL)
    @Schema(description = "审批人")
    private String approvalUserName;

    /**
     * 审批结论
     */
    @Excel(name = "审批结论", type = Type.ALL)
    @Schema(description = "审批结论")
    private String approvalConclusion;

    /**
     * 状态
     */
    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    /**
     * 物料总数
     */
    @Excel(name = "物料总数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "物料总数")
    private Integer totalMaterials;

    /**
     * 已盘数量
     */
    @Excel(name = "已盘数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "已盘数量")
    private Integer checkedCount;

    /**
     * 未盘数量
     */
    @Excel(name = "未盘数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "未盘数量")
    private Integer uncheckedCount;

    /**
     * 完成时间
     */
    @Excel(name = "完成时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "完成时间")
    private Date finishTime;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
