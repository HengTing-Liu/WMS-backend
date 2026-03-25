package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流水号规则表(SysSerialNumber)查询请求DTO
 *
 * @author lht
 * @since 2026-03-09 14:36:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流水号规则查询请求入参")
public class SysSerialNumberQueryRequest extends BaseRequest {

    @Schema(description = "规则名称（模糊查询）")
    private String ruleName;

    @Schema(description = "规则编码（模糊查询）")
    private String ruleCode;

    @Schema(description = "状态：0-启用, 1-停用")
    private String status;

    @Schema(description = "重置类型：NEVER-从不, DAY-按天, MONTH-按月, YEAR-按年")
    private String resetType;
}
