package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import com.abclonal.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * 枚举明细表(SysEnumItem)入参
 *
 * @author lht
 * @since 2026-03-06 16:07:42
 */
@Data
@Schema(description = "SysEnumItem请求入参")
public class SysEnumItemBatchRequest extends BaseRequest {
    private List<SysEnumItemRequest> records;

    public List<SysEnumItemRequest> getRecords() {
        return records;
    }

    public void setRecords(List<SysEnumItemRequest> records) {
        this.records = records;
    }
    

}

