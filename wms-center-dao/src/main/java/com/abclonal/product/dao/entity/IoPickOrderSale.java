package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 销售提货单主表(IoPickOrderSale)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class IoPickOrderSale extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "提货单号", type = Type.ALL)
    @Schema(description = "提货单号")
    private String pickNo;

    @Excel(name = "订单号", type = Type.ALL)
    @Schema(description = "订单号")
    private String orderNo;

    @Excel(name = "要求发货时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "要求发货时间")
    private Date requireDeliveryTime;

    @Excel(name = "订单日期", type = Type.EXPORT, dateFormat = "yyyy-MM-dd")
    @Schema(description = "订单日期")
    private Date orderDate;

    @Excel(name = "发货公司编码", type = Type.ALL)
    @Schema(description = "发货公司编码")
    private String outCompanyCode;

    @Excel(name = "发货仓库编码", type = Type.ALL)
    @Schema(description = "发货仓库编码")
    private String outWarehouseCode;

    private String salesUserCode;

    @Excel(name = "销售员姓名", type = Type.ALL)
    @Schema(description = "销售员姓名")
    private String salesUserName;

    private String salesDeptCode;
    private String salesDeptName;
    private String salesDeptSeq;
    private String customerCode;

    @Excel(name = "客户名称", type = Type.ALL)
    @Schema(description = "客户名称")
    private String customerName;

    private String customerType;
    private String orderOrgCode;
    private String orderOrgName;
    private String orderOrgU8cCode;

    @Excel(name = "是否特殊要求", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否特殊要求")
    private Integer isSpecialRequire;

    @Excel(name = "发货要求汇总", type = Type.EXPORT)
    @Schema(description = "发货要求汇总")
    private String deliveryRequireSummary;

    private String deliveryHub;
    private String consignee;
    private String phone;
    private String mobile;
    private String country;
    private String province;
    private String city;
    private String address;
    private String settlementCurrency;

    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    private String erpSyncRemark;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
