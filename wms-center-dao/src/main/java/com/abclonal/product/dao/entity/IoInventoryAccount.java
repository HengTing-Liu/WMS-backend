package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 库存出入台账表(IoInventoryAccount)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class IoInventoryAccount extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "单据号", type = Type.ALL)
    @Schema(description = "单据号")
    private String accountNo;

    @Excel(name = "申请日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "申请日期")
    private Date applyDate;

    private Long applyDeptId;

    @Excel(name = "申请部门", type = Type.ALL)
    @Schema(description = "申请部门")
    private String applyDeptName;

    private Long applicantId;

    @Excel(name = "申请人", type = Type.ALL)
    @Schema(description = "申请人")
    private String applicantName;

    @Excel(name = "收发类型", type = Type.ALL)
    @Schema(description = "收发类型（入库/出库）")
    private String ioType;

    @Excel(name = "业务类型", type = Type.ALL)
    @Schema(description = "业务类型")
    private String bizType;

    @Excel(name = "出库公司编码", type = Type.ALL)
    @Schema(description = "出库公司编码")
    private String outCompanyCode;

    @Excel(name = "出库仓库编码", type = Type.ALL)
    @Schema(description = "出库仓库编码")
    private String outWarehouseCode;

    @Excel(name = "入库公司编码", type = Type.ALL)
    @Schema(description = "入库公司编码")
    private String inCompanyCode;

    @Excel(name = "入库仓库编码", type = Type.ALL)
    @Schema(description = "入库仓库编码")
    private String inWarehouseCode;

    @Excel(name = "审批时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审批时间")
    private Date approvalTime;

    private Long approvalUserId;
    private String approvalUserName;
    private String approvalConclusion;

    @Excel(name = "是否需要快递", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否需要快递")
    private Integer needExpress;

    @Excel(name = "收货人", type = Type.ALL)
    @Schema(description = "收货人")
    private String consignee;

    @Excel(name = "手机号码", type = Type.ALL)
    @Schema(description = "手机号码")
    private String phone;

    private String country;
    private String province;
    private String city;
    private String district;

    @Excel(name = "详细地址", type = Type.ALL)
    @Schema(description = "详细地址")
    private String address;

    @Excel(name = "是否特殊要求", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否特殊要求")
    private Integer isSpecialRequire;

    @Excel(name = "要求发货时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "要求发货时间")
    private Date requireDeliveryTime;

    @Excel(name = "其他发货要求", type = Type.EXPORT)
    @Schema(description = "其他发货要求")
    private String otherRequire;

    @Excel(name = "来源类型", type = Type.ALL)
    @Schema(description = "来源类型")
    private String sourceType;

    @Excel(name = "来源单据号", type = Type.ALL)
    @Schema(description = "来源单据号")
    private String sourceNo;

    private Long sourceId;

    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    @Excel(name = "出库完成日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "出库完成日期")
    private Date outFinishDate;

    private Long outOperatorId;
    private String outOperatorName;

    @Excel(name = "入库完成日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "入库完成日期")
    private Date inFinishDate;

    private Long inOperatorId;
    private String inOperatorName;

    private String erpSyncRemark;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
