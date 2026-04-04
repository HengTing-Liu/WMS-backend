package com.abtk.product.service.system.impl;

import com.abtk.product.api.domain.request.sys.SysSerialNumberQueryRequest;
import com.abtk.product.api.domain.request.sys.SysSerialNumberRequest;
import com.abtk.product.api.domain.response.sys.SysSerialNumberResponse;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.dao.entity.SysSerialNumber;
import com.abtk.product.dao.mapper.SysSerialNumberMapper;
import com.abtk.product.domain.converter.SysSerialNumberConverter;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.service.system.service.ISysSerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流水号规则表(SysSerialNumber)表服务实现类
 *
 * @author lht
 * @since 2026-03-09 15:20:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysSerialNumberServiceImpl implements ISysSerialNumberService {

    @Autowired
    private SysSerialNumberMapper sysSerialNumberMapper;

    @Autowired
    private I18nService i18nService;

    // ==================== 查询方法 ====================

    @Override
    public SysSerialNumberResponse queryById(Long id) {
        SysSerialNumber entity = sysSerialNumberMapper.queryById(id);
        return SysSerialNumberConverter.INSTANCE.entityToResponse(entity);
    }

    @Override
    public List<SysSerialNumberResponse> queryByCondition(SysSerialNumberRequest request) {
        SysSerialNumber condition = SysSerialNumberConverter.INSTANCE.requestToEntity(request);
        List<SysSerialNumber> list = sysSerialNumberMapper.queryAll(condition);
        return list.stream()
                .map(SysSerialNumberConverter.INSTANCE::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysSerialNumberResponse> queryByCondition(SysSerialNumberQueryRequest queryRequest) {
        SysSerialNumber condition = SysSerialNumberConverter.INSTANCE.queryRequestToEntity(queryRequest);
        List<SysSerialNumber> list = sysSerialNumberMapper.queryAll(condition);
        return list.stream()
                .map(SysSerialNumberConverter.INSTANCE::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long count(SysSerialNumberRequest request) {
        SysSerialNumber condition = SysSerialNumberConverter.INSTANCE.requestToEntity(request);
        return sysSerialNumberMapper.count(condition);
    }

    @Override
    public boolean existsById(Long id) {
        return sysSerialNumberMapper.queryById(id) != null;
    }

    // ==================== 新增方法 ====================

    @Override
    public SysSerialNumberResponse insert(SysSerialNumberRequest request, String createBy) {
        SysSerialNumber entity = SysSerialNumberConverter.INSTANCE.requestToEntity(request);
        // 初始化当前值为起始值（新增时总是从起始值开始）
        if (entity.getStartValue() != null) {
            entity.setCurrentValue(entity.getStartValue());
        } else if (entity.getCurrentValue() == null) {
            entity.setCurrentValue(1L);
        }
        entity.setCreateBy(createBy);
        entity.setCreateTime(new java.util.Date());
        sysSerialNumberMapper.insert(entity);
        return SysSerialNumberConverter.INSTANCE.entityToResponse(entity);
    }

    // ==================== 修改方法 ====================

    @Override
    public int update(SysSerialNumberRequest request, String updateBy) {
        if (request.getId() == null) {
            throw new RuntimeException(i18nService.getMessage("system.param.error"));
        }
        SysSerialNumber entity = SysSerialNumberConverter.INSTANCE.requestToEntity(request);
        entity.setUpdateBy(updateBy);
        // 编辑时不更新当前序号，currentValue 只在生成流水号时自动变更
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
        // status: 0->启用(1), 1->停用(0)
        entity.setIsEnabled("0".equals(status) ? 1 : 0);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new java.util.Date());
        return sysSerialNumberMapper.update(entity);
    }

    // ==================== 删除方法 ====================

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

    // ==================== 导出方法 ====================

    @Override
    public void export(SysSerialNumberQueryRequest queryRequest, HttpServletResponse response) {
        SysSerialNumber condition = SysSerialNumberConverter.INSTANCE.queryRequestToEntity(queryRequest);
        List<SysSerialNumber> list = sysSerialNumberMapper.queryAll(condition);
        List<SysSerialNumberResponse> responseList = list.stream()
                .map(SysSerialNumberConverter.INSTANCE::entityToResponse)
                .collect(Collectors.toList());
        ExcelUtil<SysSerialNumberResponse> util = new ExcelUtil<>(SysSerialNumberResponse.class);
        util.exportExcel(response, responseList, "流水号规则数据");
    }
}
