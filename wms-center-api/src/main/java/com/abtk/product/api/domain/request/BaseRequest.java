package com.abtk.product.api.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 15:01
 *
 */
@Data
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = 5213975453248338593L;

    /**
     * 创建者
     */
    @Schema(description = "创建者", example = "张三")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2022-01-01 01:00:00")
    private Date createTime;

    /**
     * 更新者
     */
    @Schema(description = "更新者", example = "李四")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间", example = "2022-01-01 01:00:00")
    private Date updateTime;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "请求参数")
    private Map<String, Object> params;
}
