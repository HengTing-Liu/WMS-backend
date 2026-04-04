package com.abtk.product.service.sys.service;

import java.util.Map;

/**
 * 通用数据查询服务接口
 */
public interface GenericDataService {

    /**
     * 通用数据查询
     */
    Map<String, Object> queryData(String apiUrl, String labelField, String valueField);
}
