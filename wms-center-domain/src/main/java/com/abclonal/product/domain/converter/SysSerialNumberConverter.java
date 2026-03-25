package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysSerialNumberQueryRequest;
import com.abclonal.product.api.domain.request.sys.SysSerialNumberRequest;
import com.abclonal.product.api.domain.response.sys.SysSerialNumberResponse;
import com.abclonal.product.dao.entity.SysSerialNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统流水号转换器 - 使用 MapStruct 自动映射
 *
 * @author: lht
 * @time: 2026/3/9 14:10
 */
@Mapper
public interface SysSerialNumberConverter {
    SysSerialNumberConverter INSTANCE = Mappers.getMapper(SysSerialNumberConverter.class);

    // ========== Request -> Entity ==========

    @Mappings({
        @Mapping(source = "ruleName", target = "name"),
        @Mapping(source = "ruleCode", target = "usageScope"),
        @Mapping(source = "seqLength", target = "digitLength"),
        @Mapping(source = "currentSeq", target = "currentValue"),
        @Mapping(source = "maxSeq", target = "startValue"),
        @Mapping(source = "description", target = "remark"),
        @Mapping(source = "dateFormat", target = "numberType", qualifiedByName = "dateFormatToNumberType"),
        @Mapping(source = "resetType", target = "resetRule", qualifiedByName = "resetTypeToResetRule"),
        @Mapping(source = "status", target = "isEnabled", qualifiedByName = "statusToIsEnabled")
    })
    SysSerialNumber requestToEntity(SysSerialNumberRequest request);

    // ========== Entity -> Response ==========

    @Mappings({
        @Mapping(source = "name", target = "ruleName"),
        @Mapping(source = "usageScope", target = "ruleCode"),
        @Mapping(source = "digitLength", target = "seqLength"),
        @Mapping(source = "currentValue", target = "currentSeq"),
        @Mapping(source = "startValue", target = "maxSeq"),
        @Mapping(source = "remark", target = "description"),
        @Mapping(source = "numberType", target = "dateFormat", qualifiedByName = "numberTypeToDateFormat"),
        @Mapping(source = "resetRule", target = "resetType", qualifiedByName = "resetRuleToResetType"),
        @Mapping(source = "isEnabled", target = "status", qualifiedByName = "isEnabledToStatus"),
        @Mapping(target = "step", constant = "1")
    })
    SysSerialNumberResponse entityToResponse(SysSerialNumber entity);

    // ========== QueryRequest -> Entity ==========

    @Mappings({
        @Mapping(source = "ruleName", target = "name"),
        @Mapping(source = "ruleCode", target = "usageScope"),
        @Mapping(source = "status", target = "isEnabled", qualifiedByName = "statusToIsEnabled"),
        @Mapping(source = "resetType", target = "resetRule", qualifiedByName = "resetTypeToResetRuleForQuery")
    })
    SysSerialNumber queryRequestToEntity(SysSerialNumberQueryRequest queryRequest);

    // ========== 转换方法 ==========

    @Named("dateFormatToNumberType")
    default Integer dateFormatToNumberType(String dateFormat) {
        if (dateFormat == null || dateFormat.isEmpty()) {
            return 0;
        }
        switch (dateFormat) {
            case "yyyy":
                return 1;
            case "yyyyMM":
                return 2;
            case "yyyyMMdd":
                return 3;
            default:
                return 0;
        }
    }

    @Named("numberTypeToDateFormat")
    default String numberTypeToDateFormat(Integer numberType) {
        if (numberType == null) {
            return "";
        }
        switch (numberType) {
            case 1:
                return "yyyy";
            case 2:
                return "yyyyMM";
            case 3:
                return "yyyyMMdd";
            default:
                return "";
        }
    }

    @Named("resetTypeToResetRule")
    default String resetTypeToResetRule(String resetType) {
        if (resetType == null) {
            return "0";  // 新增/修改时返回默认值
        }
        switch (resetType) {
            case "NEVER":
                return "0";
            case "YEAR":
                return "1";
            case "MONTH":
                return "2";
            case "DAY":
                return "3";
            default:
                return "0";
        }
    }

    @Named("resetTypeToResetRuleForQuery")
    default String resetTypeToResetRuleForQuery(String resetType) {
        if (resetType == null) {
            return null;  // 查询时返回null，不添加查询条件
        }
        switch (resetType) {
            case "NEVER":
                return "0";
            case "YEAR":
                return "1";
            case "MONTH":
                return "2";
            case "DAY":
                return "3";
            default:
                return null;
        }
    }

    @Named("resetRuleToResetType")
    default String resetRuleToResetType(String resetRule) {
        if (resetRule == null) {
            return "NEVER";
        }
        switch (resetRule) {
            case "0":
                return "NEVER";
            case "1":
                return "YEAR";
            case "2":
                return "MONTH";
            case "3":
                return "DAY";
            default:
                return "NEVER";
        }
    }

    @Named("statusToIsEnabled")
    default Integer statusToIsEnabled(String status) {
        if (status == null) {
            return null;
        }
        return "0".equals(status) ? 1 : 0;
    }

    @Named("isEnabledToStatus")
    default String isEnabledToStatus(Integer isEnabled) {
        if (isEnabled == null) {
            return "1";
        }
        return isEnabled == 1 ? "0" : "1";
    }

    default List<Long> idsStringToList(String ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return Stream.of(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
