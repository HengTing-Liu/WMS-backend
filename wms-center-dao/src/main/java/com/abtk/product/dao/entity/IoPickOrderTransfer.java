package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 调拨提货单主表(IoPickOrderTransfer)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class IoPickOrderTransfer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "提货单号", type = Type.ALL)
    @Schema(description = "提货单号")
    private String pickNo;

    @Excel(name = "调拨单号", type = Type.ALL)
    @Schema(description = "调拨单号")
    private String transferNo;

    @Excel(name = "调出公司编码", type = Type.ALL)
    @Schema(description = "调出公司编码")
    private String outCompanyCode;

    @Excel(name = "调出仓库编码", type = Type.ALL)
    @Schema(description = "调出仓库编码")
    private String outWarehouseCode;

    @Excel(name = "调入公司编码", type = Type.ALL)
    @Schema(description = "调入公司编码")
    private String inCompanyCode;

    @Excel(name = "调入仓库编码", type = Type.ALL)
    @Schema(description = "调入仓库编码")
    private String inWarehouseCode;

    private Long applyDeptId;

    @Excel(name = "申请部门", type = Type.ALL)
    @Schema(description = "申请部门")
    private String applyDeptName;

    private Long applicantId;

    @Excel(name = "申请人", type = Type.ALL)
    @Schema(description = "申请人")
    private String applicantName;

    @Excel(name = "申请日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "申请日期")
    private Date applyDate;

    @Excel(name = "要求发货时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "要求发货时间")
    private Date requireDeliveryTime;

    @Excel(name = "是否需要快递", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否需要快递")
    private Integer needExpress;

    private String consignee;
    private String phone;
    private String mobile;
    private String country;
    private String province;
    private String city;
    private String address;

    @Excel(name = "是否特殊要求", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否特殊要求")
    private Integer isSpecialRequire;

    @Excel(name = "其他发货要求", type = Type.EXPORT)
    @Schema(description = "其他发货要求")
    private String otherRequire;

    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    private String erpSyncRemark;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
