package com.abtk.product.api.domain.request.inv;

import com.abtk.product.api.domain.request.BaseRequest;
import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * (InvQrcodedetail)入参
 *
 * @author lht
 * @since 2026-03-02 17:07:31
 */
@Data
@Schema(description = "InvQrcodedetail请求入参")
public class InvQrcodedetailBatchRequest extends BaseRequest {
    private List<InvQrcodedetailRequest> records;

    public List<InvQrcodedetailRequest> getRecords() {
        return records;
    }

    public void setRecords(List<InvQrcodedetailRequest> records) {
        this.records = records;
    }
    

}

