package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 多库位选择表(IoLocationSelection)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class IoLocationSelection extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    @Excel(name = "来源单据表名", type = Type.ALL)
    @Schema(description = "来源单据表名")
    private String sourceTable;

    @Excel(name = "来源单据ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "来源单据ID")
    private Long sourceId;

    @Excel(name = "库存ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "库存ID")
    private Long inventoryId;

    @Excel(name = "公司编码", type = Type.ALL)
    @Schema(description = "公司编码")
    private String companyCode;

    @Excel(name = "仓库编码", type = Type.ALL)
    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Excel(name = "库位ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "库位ID")
    private Long locationId;

    @Excel(name = "库位全路径名称", type = Type.ALL)
    @Schema(description = "库位全路径名称")
    private String locationFullpathName;

    @Excel(name = "占用数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "占用数量")
    private Integer occupyQuantity;

    @Excel(name = "扫码数量", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "扫码数量")
    private Integer scanQuantity;

    private Long operatorId;

    private String operatorName;

    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
