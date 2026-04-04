package com.abtk.product.service.sys.service;

import java.util.List;
import java.util.Map;

/**
 * 字典数据服务接口
 */
public interface DictService {

    /**
     * 根据字典类型查询字典数据
     */
    List<Map<String, Object>> getDictData(String dictType);

    /**
     * 查询字典标签
     */
    String getDictLabel(String dictType, String dictValue);

    /**
     * 批量查询字典数据
     */
    Map<String, List<Map<String, Object>>> getDictDataBatch(List<String> dictTypes);
}
