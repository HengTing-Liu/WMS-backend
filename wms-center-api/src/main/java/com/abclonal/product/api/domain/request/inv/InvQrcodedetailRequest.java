package com.abclonal.product.api.domain.request.inv;

import com.abclonal.product.api.domain.request.BaseRequest;
import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * (InvQrcodedetail)入参
 *
 * @author lht
 * @since 2026-03-04 14:52:45
 */
@Data
@Schema(description = "InvQrcodedetail请求入参")
public class InvQrcodedetailRequest extends BaseRequest {
    /**
     * id
     */
          
    @Excel(name = "id", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "id")
    private Long id;
    
    /**
     * 逻辑删除
     */
          
    @Excel(name = "逻辑删除", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "逻辑删除")
    private Integer isDeleted;
    
    /**
     * qrcode
     */
          
    @Excel(name = "qrcode", type = Type.ALL)
    @Schema(description = "qrcode")
    private String qrcode;
    

}
