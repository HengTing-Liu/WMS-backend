package com.abtk.product.biz.system;

import com.abtk.product.api.domain.request.sys.SysSerialNumberQueryRequest;
import com.abtk.product.api.domain.request.sys.SysSerialNumberRequest;
import com.abtk.product.api.domain.response.sys.SysSerialNumberResponse;
import com.abtk.product.dao.entity.SysSerialNumber;
import com.abtk.product.dao.mapper.SysSerialNumberMapper;
import com.abtk.product.service.system.service.ISysSerialNumberService;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流水号规则业务层
 *
 * @author lht
 * @since 2026-03-11
 */
@Slf4j
@Component
public class SysSerialNumberBiz {

    @Autowired
    private ISysSerialNumberService sysSerialNumberService;

    @Autowired
    private SysSerialNumberMapper sysSerialNumberMapper;

    @Autowired
    private I18nService i18nService;

    /**
     * 导入流水号规则
     */
    @Transactional(rollbackFor = Exception.class)
    public String importSerialNumber(List<SysSerialNumberRequest> list, boolean updateSupport, String operName) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException(i18nService.getMessage("import.data.empty"));
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SysSerialNumberRequest request : list) {
            try {
                // 根据规则编码查询是否已存在
                SysSerialNumberQueryRequest query = new SysSerialNumberQueryRequest();
                query.setRuleCode(request.getRuleCode());
                List<SysSerialNumberResponse> existingList = sysSerialNumberService.queryByCondition(query);

                if (existingList == null || existingList.isEmpty()) {
                    // 新增
                    sysSerialNumberService.insert(request, operName);
                    successNum++;
                } else if (updateSupport) {
                    // 更新
                    SysSerialNumberResponse existing = existingList.get(0);
                    request.setId(existing.getId());
                    sysSerialNumberService.update(request, operName);
                    successNum++;
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、规则 ").append(request.getRuleCode()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、规则 ").append(request.getRuleCode()).append(" 导入失败：").append(e.getMessage());
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "导入失败！共 " + failureNum + " 条数据：");
            throw new RuntimeException(failureMsg.toString());
        }

        successMsg.insert(0, "导入成功！共 " + successNum + " 条数据");
        return successMsg.toString();
    }

    // ==================== 单条生成 ====================

    /**
     * 根据规则名称生成流水号
     */
    @Transactional(rollbackFor = Exception.class)
    public String generateSerialNumber(String ruleName, String updateBy) {
        SysSerialNumber rule = findRuleByName(ruleName);
        return doGenerateSerialNumber(rule, updateBy);
    }

    /**
     * 根据应用表单字段生成流水号
     */
    @Transactional(rollbackFor = Exception.class)
    public String generateSerialNumberByApplyFormField(String applyFormField, String updateBy) {
        SysSerialNumber rule = findRuleByApplyFormField(applyFormField);
        return doGenerateSerialNumber(rule, updateBy);
    }

    /**
     * 根据规则编码生成流水号
     */
    @Transactional(rollbackFor = Exception.class)
    public String generateSerialNumberByRuleCode(String ruleCode, String updateBy) {
        SysSerialNumber rule = findRuleByRuleCode(ruleCode);
        return doGenerateSerialNumber(rule, updateBy);
    }

    /**
     * 根据规则编码预览下一个流水号（只计算，不更新数据库）
     */
    public String previewSerialNumberByRuleCode(String ruleCode) {
        SysSerialNumber rule = findRuleByRuleCode(ruleCode);
        return doPreviewSerialNumber(rule);
    }

    /**
     * 根据规则名称预览下一个流水号（只计算，不更新数据库）
     */
    public String previewSerialNumberByRuleName(String ruleName) {
        SysSerialNumber rule = findRuleByName(ruleName);
        return doPreviewSerialNumber(rule);
    }

    // ==================== 批量生成 ====================

    /**
     * 根据规则名称批量生成流水号
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> batchGenerateSerialNumber(String ruleName, int count, String updateBy) {
        if (count <= 0 || count > 1000) {
            throw new RuntimeException(i18nService.getMessage("serial.number.batch.count.invalid"));
        }

        SysSerialNumber rule = findRuleByName(ruleName);

        List<String> serialNumbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            serialNumbers.add(doGenerateSerialNumber(rule, updateBy));
        }
        return serialNumbers;
    }

    // ==================== 私有方法 ====================

    /**
     * 根据规则名称查找规则
     */
    private SysSerialNumber findRuleByName(String ruleName) {
        SysSerialNumberQueryRequest query = new SysSerialNumberQueryRequest();
        query.setRuleName(ruleName);
        List<SysSerialNumberResponse> list = sysSerialNumberService.queryByCondition(query);

        if (list == null || list.isEmpty()) {
            throw new RuntimeException(i18nService.getMessage("serial.number.rule.not.found") + ": " + ruleName);
        }

        SysSerialNumberResponse response = list.get(0);
        if (!"0".equals(response.getStatus())) {
            throw new RuntimeException(i18nService.getMessage("serial.number.rule.disabled") + ": " + ruleName);
        }

        // Response 转 Entity
        return toEntity(response);
    }

    /**
     * 根据应用表单字段查找规则
     */
    private SysSerialNumber findRuleByApplyFormField(String applyFormField) {
        SysSerialNumberQueryRequest query = new SysSerialNumberQueryRequest();
        query.setApplyFormField(applyFormField);
        List<SysSerialNumberResponse> list = sysSerialNumberService.queryByCondition(query);

        if (list == null || list.isEmpty()) {
            throw new RuntimeException(i18nService.getMessage("serial.number.rule.not.found") + ": " + applyFormField);
        }

        SysSerialNumberResponse response = list.get(0);
        if (!"0".equals(response.getStatus())) {
            throw new RuntimeException(i18nService.getMessage("serial.number.rule.disabled") + ": " + applyFormField);
        }

        // Response 转 Entity
        return toEntity(response);
    }

    /**
     * 根据规则编码查找规则
     */
    private SysSerialNumber findRuleByRuleCode(String ruleCode) {
        SysSerialNumberQueryRequest query = new SysSerialNumberQueryRequest();
        query.setRuleCode(ruleCode);
        List<SysSerialNumberResponse> list = sysSerialNumberService.queryByCondition(query);

        if (list == null || list.isEmpty()) {
            throw new RuntimeException(i18nService.getMessage("serial.number.rule.not.found") + ": " + ruleCode);
        }

        SysSerialNumberResponse response = list.get(0);
        if (!"0".equals(response.getStatus())) {
            throw new RuntimeException(i18nService.getMessage("serial.number.rule.disabled") + ": " + ruleCode);
        }

        // Response 转 Entity
        return toEntity(response);
    }

    /**
     * Response 转 Request（用于 Converter）
     */
    private SysSerialNumberRequest toRequest(SysSerialNumberResponse response) {
        SysSerialNumberRequest request = new SysSerialNumberRequest();
        request.setId(response.getId());
        request.setRuleCode(response.getRuleCode());
        request.setRuleName(response.getRuleName());
        request.setPrefix(response.getPrefix());
        request.setDateFormat(response.getDateFormat());
        request.setSeqLength(response.getSeqLength());
        request.setCurrentSeq(response.getCurrentSeq());
        request.setMaxSeq(response.getMaxSeq());
        request.setSuffix(response.getSuffix());
        request.setResetType(response.getResetType());
        request.setStatus(response.getStatus());
        request.setDescription(response.getDescription());
        return request;
    }

    private SysSerialNumber toEntity(SysSerialNumberResponse response) {
        SysSerialNumber entity = new SysSerialNumber();
        entity.setId(response.getId());
        entity.setUsageScope(response.getRuleCode());
        entity.setName(response.getRuleName());
        entity.setPrefix(response.getPrefix());
        entity.setNumberType(dateFormatToNumberType(response.getDateFormat()));
        entity.setDigitLength(response.getSeqLength());
        entity.setCurrentValue(response.getCurrentSeq());
        entity.setStartValue(response.getMaxSeq());
        entity.setSuffix(response.getSuffix());
        entity.setResetRule(resetTypeToResetRule(response.getResetType()));
        entity.setIsEnabled("0".equals(response.getStatus()) ? 1 : 0);
        return entity;
    }

    private Integer dateFormatToNumberType(String dateFormat) {
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

    private String resetTypeToResetRule(String resetType) {
        if (resetType == null) {
            return "0";
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

    /**
     * 预览下一个流水号（只计算，不更新数据库）
     */
    private String doPreviewSerialNumber(SysSerialNumber rule) {
        if (rule == null || rule.getId() == null) {
            throw new RuntimeException(i18nService.getMessage("serial.number.rule.not.found"));
        }

        // 获取日期部分
        String datePart = getDatePart(rule.getNumberType());

        // 获取当前重置周期标识
        String currentResetKey = getResetKey(rule.getResetRule());

        // 获取上次生成的重置周期标识
        String lastResetKey = rule.getLastResetKey();

        Long currentValue;

        // 如果重置周期变化，序号重置为start_value
        if (currentResetKey != null && !currentResetKey.equals(lastResetKey)) {
            currentValue = rule.getStartValue();
        } else {
            currentValue = rule.getCurrentValue();
            if (currentValue == null) {
                currentValue = rule.getStartValue();
            }
        }

        // 构建序号部分
        String numberPart = formatNumber(currentValue, rule.getDigitLength());

        // 构建完整流水号
        String prefix = rule.getPrefix() != null ? rule.getPrefix() : "";
        String suffix = rule.getSuffix() != null ? rule.getSuffix() : "";
        return prefix + datePart + numberPart + suffix;
    }

    /**
     * 执行生成流水号
     * 格式：prefix + 日期部分 + 序号 + suffix
     */
    private synchronized String doGenerateSerialNumber(SysSerialNumber rule, String updateBy) {
        // 先预览计算
        String serialNumber = doPreviewSerialNumber(rule);

        // 获取当前重置周期标识（用于更新）
        String currentResetKey = getResetKey(rule.getResetRule());

        // 更新规则 - 同时设置更新人和更新时间
        Long currentValue = rule.getCurrentValue();
        if (currentValue == null) {
            currentValue = rule.getStartValue();
        }
        rule.setCurrentValue(currentValue + 1);
        rule.setLastResetKey(currentResetKey);
        rule.setUpdateBy(updateBy);
        rule.setUpdateTime(new Date());
        sysSerialNumberMapper.update(rule);

        return serialNumber;
    }

    /**
     * 根据number_type获取日期部分
     */
    private String getDatePart(Integer numberType) {
        if (numberType == null || numberType == 0) {
            return "";
        }

        Date now = new Date();
        switch (numberType) {
            case 1:
                return new SimpleDateFormat("yyyy").format(now);
            case 2:
                return new SimpleDateFormat("yyyyMM").format(now);
            case 3:
                return new SimpleDateFormat("yyyyMMdd").format(now);
            default:
                return "";
        }
    }

    /**
     * 根据reset_rule获取重置周期标识
     */
    private String getResetKey(String resetRuleStr) {
        if (resetRuleStr == null || resetRuleStr.isEmpty() || "0".equals(resetRuleStr)) {
            return null;
        }

        int resetRule;
        try {
            resetRule = Integer.parseInt(resetRuleStr);
        } catch (NumberFormatException e) {
            return null;
        }

        Date now = new Date();
        switch (resetRule) {
            case 1:
                return new SimpleDateFormat("yyyy").format(now);
            case 2:
                return new SimpleDateFormat("yyyyMM").format(now);
            case 3:
                return new SimpleDateFormat("yyyyMMdd").format(now);
            default:
                return null;
        }
    }

    /**
     * 格式化序号
     */
    private String formatNumber(Long number, Integer digitLength) {
        if (number == null) {
            number = 0L;
        }
        if (digitLength == null || digitLength <= 0) {
            return String.valueOf(number);
        }
        return String.format("%0" + digitLength + "d", number);
    }
}
