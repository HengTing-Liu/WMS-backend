package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 随货物品清单表(IoGoodsWith)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class IoGoodsWith extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "订单号", type = Type.ALL)
    @Schema(description = "订单号")
    private String pickNo;

    @Excel(name = "客户编号", type = Type.ALL)
    @Schema(description = "客户编号")
    private String customerCode;

    @Excel(name = "客户名称", type = Type.ALL)
    @Schema(description = "客户名称")
    private String customerName;

    @Excel(name = "收货人", type = Type.ALL)
    @Schema(description = "收货人")
    private String consignee;

    @Excel(name = "收货电话", type = Type.ALL)
    @Schema(description = "收货电话")
    private String phone;

    @Excel(name = "收货手机", type = Type.ALL)
    @Schema(description = "收货手机")
    private String mobile;

    private String country;
    private String province;
    private String city;

    @Excel(name = "收货地址", type = Type.ALL)
    @Schema(description = "收货地址")
    private String address;

    @Excel(name = "随货内容", type = Type.ALL)
    @Schema(description = "随货内容")
    private String goodsContent;

    private Long outboundId;

    @Excel(name = "是否特殊要求", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否特殊要求")
    private Integer isSpecialRequire;

    @Excel(name = "要求发货时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "要求发货时间")
    private Date requireDeliveryTime;

    @Excel(name = "其他发货要求", type = Type.EXPORT)
    @Schema(description = "其他发货要求")
    private String otherRequire;

    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
