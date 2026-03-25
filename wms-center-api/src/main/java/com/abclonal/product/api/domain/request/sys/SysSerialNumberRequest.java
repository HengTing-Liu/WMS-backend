package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 流水号规则表(SysSerialNumber)入参
 *
 * @author lht
 * @since 2026-03-09 14:36:00
 */
@Data
@Schema(description = "流水号规则请求入参")
public class SysSerialNumberRequest extends BaseRequest {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 规则编码
     */
    @Schema(description = "规则编码")
    private String ruleCode;

    /**
     * 规则名称
     */
    @Schema(description = "规则名称")
    private String ruleName;

    /**
     * 前缀
     */
    @Schema(description = "前缀")
    private String prefix;

    /**
     * 日期格式：空-无日期, yyyy-年, yyyyMM-年月, yyyyMMdd-年月日
     */
    @Schema(description = "日期格式")
    private String dateFormat;

    /**
     * 序列号长度
     */
    @Schema(description = "序列号长度")
    private Integer seqLength;

    /**
     * 当前序号
     */
    @Schema(description = "当前序号")
    private Long currentSeq;

    /**
     * 最大序号
     */
    @Schema(description = "最大序号")
    private Long maxSeq;

    /**
     * 后缀
     */
    @Schema(description = "后缀")
    private String suffix;

    /**
     * 重置类型：DAY-按天, MONTH-按月, YEAR-按年, NEVER-从不
     */
    @Schema(description = "重置类型")
    private String resetType;

    /**
     * 状态：0-启用, 1-停用
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;
}
