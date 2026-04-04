package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 库存盘点库位计划表(StStocktakeLocation)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class StStocktakeLocation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "盘点ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "盘点ID")
    private Long stocktakeId;

    @Excel(name = "库位ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "库位ID")
    private Long locationId;

    @Excel(name = "库位等级", type = Type.ALL)
    @Schema(description = "库位等级")
    private String locationGrade;

    @Excel(name = "库位类型", type = Type.ALL)
    @Schema(description = "库位类型")
    private String locationType;

    @Excel(name = "库位全路径名称", type = Type.ALL)
    @Schema(description = "库位全路径名称")
    private String locationFullpathName;

    @Excel(name = "计划开始时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "计划开始时间")
    private Date planStartTime;

    @Excel(name = "计划完成时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "计划完成时间")
    private Date planFinishTime;

    @Excel(name = "实际开始时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "实际开始时间")
    private Date actualStartTime;

    @Excel(name = "实际完成时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "实际完成时间")
    private Date actualFinishTime;

    private Long operatorId;

    @Excel(name = "完工操作员", type = Type.ALL)
    @Schema(description = "完工操作员")
    private String operatorName;

    @Excel(name = "状态", type = Type.ALL)
    @Schema(description = "状态")
    private String status;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
