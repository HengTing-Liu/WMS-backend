package com.abtk.product.api.domain.request.sys;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 执行发布请求
 */
@Data
public class MetaPublishExecuteRequest {

    /** 发布编码（plan 返回的编码） */
    @NotBlank(message = "发布编码不能为空")
    private String publishCode;

    /** 是否强制执行 */
    private Boolean forced = false;

    /** 需要执行的 detail ID 列表（留空则执行全部） */
    private List<Long> detailIds;
}
