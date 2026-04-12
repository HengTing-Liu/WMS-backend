package com.abtk.product.api.domain.request.sys;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 发布历史查询请求
 */
@Data
public class MetaPublishQueryRequest {

    /** 表编码 */
    private String tableCode;

    /** 发布状态 */
    private String status;

    /** 发布人 */
    private String publishBy;

    /** 开始时间 */
    private LocalDateTime beginTime;

    /** 结束时间 */
    private LocalDateTime endTime;
}
