package com.abtk.product.api.domain.request.sys;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发布计划请求
 */
@Data
public class MetaPublishPlanRequest {

    /** 目标表编码列表 */
    @NotEmpty(message = "表编码不能为空")
    private List<@NotBlank(message = "表编码不能为空") String> tableCodes;

    /** 是否强制执行（忽略破坏性警告） */
    private Boolean forced = false;

    /** 备注 */
    private String remarks;
}
