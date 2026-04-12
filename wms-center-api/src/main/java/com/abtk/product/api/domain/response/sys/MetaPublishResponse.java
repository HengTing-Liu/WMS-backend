package com.abtk.product.api.domain.response.sys;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 发布记录响应
 */
@Data
@Accessors(chain = true)
public class MetaPublishResponse {

    private Long id;
    private String publishCode;
    private String tableCode;
    private String tableName;
    private Integer version;
    private String status;
    private Integer totalSqls;
    private Integer successSqls;
    private Integer failedSqls;
    private String errorMessage;
    private Boolean forced;
    private String publishBy;
    private String publishByName;
    private LocalDateTime publishTime;
    private String remark;
    private LocalDateTime createdAt;

    /** 明细列表 */
    private List<MetaPublishDetailResponse> details;

    @Data
    @Accessors(chain = true)
    public static class MetaPublishDetailResponse {
        private Long id;
        private Integer seq;
        private String sqlType;
        private String sqlText;
        private String riskLevel;
        private Integer executionTime;
        private String resultStatus;
        private String errorMessage;
        private LocalDateTime executedAt;
    }
}
