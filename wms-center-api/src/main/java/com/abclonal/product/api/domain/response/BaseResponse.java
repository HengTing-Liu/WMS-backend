package com.abclonal.product.api.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/10 10:10
 *
 */
@Data
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1251024487866975151L;

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
     * 更新者
     */
    @Schema(description = "备注", example = "sss")
    private String remark;
}
