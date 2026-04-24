package com.abtk.product.api.domain.response.sys;

import com.abtk.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 流水号规则表(SysSerialNumber)出参
 *
 * @author lht
 * @since 2026-03-09 14:37:00
 */
@Data
@Schema(description = "流水号规则响应出参")
public class SysSerialNumberResponse extends BaseResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "规则编码")
    private String ruleCode;

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "前缀")
    private String prefix;

    @Schema(description = "日期格式")
    private String dateFormat;

    @Schema(description = "序列号长度")
    private Integer seqLength;

    @Schema(description = "当前序号")
    private Long currentSeq;

    @Schema(description = "最大序号")
    private Long maxSeq;

    @Schema(description = "上次重置周期标识")
    private String lastResetKey;

    @Schema(description = "后缀")
    private String suffix;

    @Schema(description = "步长")
    private Integer step;

    @Schema(description = "重置类型")
    private String resetType;

    @Schema(description = "状态：0-启用, 1-停用")
    private String status;

    @Schema(description = "应用表单字段")
    private String applyFormField;

    @Schema(description = "描述")
    private String description;
}
