package com.abtk.product.api.domain.request.inv;

import com.abtk.product.api.domain.request.BaseRequest;
import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 10:23
 *
 */
@Data
@Schema(description = "用户信息请求入参")
public class CreateQrcodeRequest extends BaseRequest {
    /** 货号 */
    @Excel(name = "货号", type = Excel.Type.EXPORT, cellType = Excel.ColumnType.NUMERIC, prompt = "货号")
    @Schema(description = "货号", example = "A0001")
    private String catalogno;

    /** 产品名称 */
    @Excel(name = "产品名称", type = Excel.Type.IMPORT)
    @Schema(description = "产品名称", example = "101")
    private String productname;

    /** 批号 */
    @Excel(name = "批号")
    @Schema(description = "批号", example = "11101101011")
    private String batchid;

    @Excel(name = "规格")
    @Schema(description = "规格", example = "50")
    private String size;

    /** 单位 */
    @Excel(name = "单位")
    @Schema(description = "单位", example = "U")
    private String unit;

    /** 用户邮箱 */
    @Excel(name = "创建数量")
    @Schema(description = "创建数量", example = "100")
    private int qty;


}
