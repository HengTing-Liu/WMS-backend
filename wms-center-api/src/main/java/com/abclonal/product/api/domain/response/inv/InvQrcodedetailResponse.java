package com.abclonal.product.api.domain.response.inv;

import com.abclonal.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * (InvQrcodedetail)出参
 *
 * @author lht
 * @since 2026-03-02 14:07:59
 */
@Data
@Schema(description = "InvQrcodedetail请求出参")
public class InvQrcodedetailResponse extends BaseResponse {
    /**
     * 
     */
     @Schema(description = "")
    private Long id;
    
    /**
     * 逻辑删除
     */
     @Schema(description = "逻辑删除")
    private Integer isDeleted;
    
    /**
     * 
     */
     @Schema(description = "")
    private String qrcode;
    

}

