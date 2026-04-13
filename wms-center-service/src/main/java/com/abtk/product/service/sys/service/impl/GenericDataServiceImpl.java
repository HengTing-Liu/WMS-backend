package com.abtk.product.service.sys.service.impl;

import com.abtk.product.dao.mapper.DynamicMapper;
import com.abtk.product.dao.util.SqlInjectionValidator;
import com.abtk.product.service.sys.service.GenericDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用数据查询服务实现
 */
@Slf4j
@Service
public class GenericDataServiceImpl implements GenericDataService {

    @Autowired
    private DynamicMapper dynamicMapper;

    @Override
    public Map<String, Object> queryData(String apiUrl, String labelField, String valueField) {
        log.info("通用数据查询, apiUrl={}, labelField={}, valueField={}", apiUrl, labelField, valueField);
        
        // 从apiUrl解析表名
        String table = apiUrl;
        if (apiUrl != null && apiUrl.contains("/")) {
            String[] parts = apiUrl.split("/");
            table = parts[parts.length - 1];
        }
        
        // P0-3 修复：表名必须通过白名单校验，防止SQL注入
        SqlInjectionValidator.validateTable(table);
        // P0-3 修复：字段名必须通过白名单校验
        SqlInjectionValidator.validateField(labelField);
        SqlInjectionValidator.validateField(valueField);
        
        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("del_flag", "0");
        
        // 已实现查询逻辑
        List<Map<String, Object>> dataList = dynamicMapper.selectList(table, params, "del_flag", null);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", "200");
        result.put("message", "success");
        result.put("data", dataList);
        return result;
    }
}
