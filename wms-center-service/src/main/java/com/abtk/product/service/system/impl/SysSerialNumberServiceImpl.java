package com.abtk.product.service.system.impl;

import com.abtk.product.api.domain.request.sys.SysSerialNumberQueryRequest;
import com.abtk.product.api.domain.request.sys.SysSerialNumberRequest;
import com.abtk.product.api.domain.response.sys.SysSerialNumberResponse;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.dao.entity.SysSerialNumber;
import com.abtk.product.dao.mapper.SysSerialNumberMapper;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.service.system.service.ISysSerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysSerialNumberServiceImpl implements ISysSerialNumberService {

    @Autowired
    private SysSerialNumberMapper sysSerialNumberMapper;

    @Autowired
    private I18nService i18nService;

    @Override
    public SysSerialNumberResponse queryById(Long id) {
        SysSerialNumber entity = sysSerialNumberMapper.queryById(id);
        return toResponse(entity);
    }

    @Override
    public List<SysSerialNumberResponse> queryByCondition(SysSerialNumberRequest request) {
        SysSerialNumber condition = buildCondition(request);
        List<SysSerialNumber> list = sysSerialNumberMapper.queryAll(condition);
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<SysSerialNumberResponse> queryByCondition(SysSerialNumberQueryRequest queryRequest) {
        SysSerialNumber condition = buildQueryCondition(queryRequest);
        List<SysSerialNumber> list = sysSerialNumberMapper.queryAll(condition);
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public long count(SysSerialNumberRequest request) {
        SysSerialNumber condition = new SysSerialNumber();
        if (request.getId() != null) condition.setId(request.getId());
        if (request.getRuleName() != null) condition.setName(request.getRuleName());
        if (request.getPrefix() != null) condition.setPrefix(request.getPrefix());
        if (request.getSuffix() != null) condition.setSuffix(request.getSuffix());
        if (request.getStatus() != null) condition.setIsEnabled("0".equals(request.getStatus()) ? 1 : 0);
        return sysSerialNumberMapper.count(condition);
    }

    private SysSerialNumber buildCondition(SysSerialNumberRequest request) {
        SysSerialNumber condition = new SysSerialNumber();
        if (request.getId() != null) condition.setId(request.getId());
        if (request.getRuleCode() != null) condition.setUsageScope(request.getRuleCode());
        if (request.getRuleName() != null) condition.setName(request.getRuleName());
        if (request.getPrefix() != null) condition.setPrefix(request.getPrefix());
        if (request.getSuffix() != null) condition.setSuffix(request.getSuffix());
        if (request.getSeqLength() != null) condition.setDigitLength(request.getSeqLength());
        if (request.getMaxSeq() != null) condition.setStartValue(request.getMaxSeq());
        if (request.getCurrentSeq() != null) condition.setCurrentValue(request.getCurrentSeq());
        if (request.getDateFormat() != null) condition.setNumberType(dateFormatToNumberType(request.getDateFormat()));
        if (request.getResetType() != null) condition.setResetRule(resetTypeToResetRule(request.getResetType()));
        if (request.getStatus() != null) condition.setIsEnabled("0".equals(request.getStatus()) ? 1 : 0);
        return condition;
    }

    private SysSerialNumber buildQueryCondition(SysSerialNumberQueryRequest request) {
        SysSerialNumber condition = new SysSerialNumber();
        if (request == null) return condition;
        if (request.getRuleName() != null) condition.setName(request.getRuleName());
        if (request.getRuleCode() != null) condition.setUsageScope(request.getRuleCode());
        if (request.getStatus() != null) condition.setIsEnabled("0".equals(request.getStatus()) ? 1 : 0);
        if (request.getResetType() != null) condition.setResetRule(resetTypeToResetRule(request.getResetType()));
        return condition;
    }

    private Integer dateFormatToNumberType(String dateFormat) {
        if (dateFormat == null || dateFormat.isEmpty()) return 0;
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

    private String numberTypeToDateFormat(Integer numberType) {
        if (numberType == null) return "";
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

    private String resetTypeToResetRule(String resetType) {
        if (resetType == null) return "0";
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

    private String resetRuleToResetType(String resetRule) {
        if (resetRule == null) return "NEVER";
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

    private String isEnabledToStatus(Integer isEnabled) {
        if (isEnabled == null) return "1";
        return isEnabled == 1 ? "0" : "1";
    }

    private SysSerialNumberResponse toResponse(SysSerialNumber entity) {
        if (entity == null) return null;
        SysSerialNumberResponse resp = new SysSerialNumberResponse();
        resp.setId(entity.getId());
        resp.setRuleCode(entity.getUsageScope());
        resp.setRuleName(entity.getName());
        resp.setPrefix(entity.getPrefix());
        resp.setDateFormat(numberTypeToDateFormat(entity.getNumberType()));
        resp.setSeqLength(entity.getDigitLength());
        resp.setCurrentSeq(entity.getCurrentValue());
        resp.setMaxSeq(entity.getStartValue());
        resp.setSuffix(entity.getSuffix());
        resp.setResetType(resetRuleToResetType(entity.getResetRule()));
        resp.setStatus(isEnabledToStatus(entity.getIsEnabled()));
        resp.setStep(1);
        resp.setCreateBy(entity.getCreateBy());
        resp.setCreateTime(entity.getCreateTime());
        resp.setUpdateBy(entity.getUpdateBy());
        resp.setUpdateTime(entity.getUpdateTime());
        return resp;
    }

    @Override
    public boolean existsById(Long id) {
        return sysSerialNumberMapper.queryById(id) != null;
    }

    @Override
    public SysSerialNumberResponse insert(SysSerialNumberRequest request, String createBy) {
        SysSerialNumber entity = buildCondition(request);
        if (entity.getStartValue() != null) {
            entity.setCurrentValue(entity.getStartValue());
        } else if (entity.getCurrentValue() == null) {
            entity.setCurrentValue(1L);
        }
        entity.setCreateBy(createBy);
        entity.setCreateTime(new java.util.Date());
        sysSerialNumberMapper.insert(entity);
        return toResponse(entity);
    }

    @Override
    public int update(SysSerialNumberRequest request, String updateBy) {
        if (request.getId() == null) {
            throw new RuntimeException(i18nService.getMessage("system.param.error"));
        }
        SysSerialNumber entity = buildCondition(request);
        entity.setUpdateBy(updateBy);
        entity.setCurrentValue(null);
        return sysSerialNumberMapper.update(entity);
    }

    @Override
    public int changeStatus(Long id, String status, String updateBy) {
        if (id == null) {
            throw new RuntimeException(i18nService.getMessage("system.param.error"));
        }
        SysSerialNumber entity = new SysSerialNumber();
        entity.setId(id);
        entity.setIsEnabled("0".equals(status) ? 1 : 0);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new java.util.Date());
        return sysSerialNumberMapper.update(entity);
    }

    @Override
    public boolean logicDeleteById(Long id, String updateBy) {
        return sysSerialNumberMapper.deleteById(id, updateBy) > 0;
    }

    @Override
    public int logicDeleteBatchByIds(List<Long> ids, String updateBy) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return sysSerialNumberMapper.deleteBatchByIds(ids, updateBy);
    }

    @Override
    public void export(SysSerialNumberQueryRequest queryRequest, HttpServletResponse response) {
        SysSerialNumber condition = buildQueryCondition(queryRequest);
        List<SysSerialNumber> list = sysSerialNumberMapper.queryAll(condition);
        List<SysSerialNumberResponse> responseList = list.stream().map(this::toResponse).collect(Collectors.toList());
        ExcelUtil<SysSerialNumberResponse> util = new ExcelUtil<>(SysSerialNumberResponse.class);
        util.exportExcel(response, responseList, "Serial Rule Data");
    }
}
