package com.abtk.product.api.domain.response.inv;

import com.abtk.product.api.domain.response.BaseResponse;
import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 16:45
 *
 */
@Data
@Schema(description = "用户信息响应出参")
public class CreateQrcodeResponse extends BaseResponse {

    private List<String> qrcodelist;

    // Getter 方法
    public List<String> getQrcodelist() {
        return qrcodelist;
    }

    // Setter 方法
    public void setQrcodelist(List<String> qrcodelist) {
        this.qrcodelist = qrcodelist;
    }

}
