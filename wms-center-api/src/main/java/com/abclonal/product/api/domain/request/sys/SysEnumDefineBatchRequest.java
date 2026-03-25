package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import com.abclonal.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * 枚举定义表(SysEnumDefine)入参
 *
 * @author lht
 * @since 2026-03-06 14:36:19
 */
@Data
@Schema(description = "SysEnumDefine请求入参")
public class SysEnumDefineBatchRequest extends BaseRequest {
    private List<SysEnumDefineRequest> records;

    public List<SysEnumDefineRequest> getRecords() {
        return records;
    }

    public void setRecords(List<SysEnumDefineRequest> records) {
        this.records = records;
    }
    

}

