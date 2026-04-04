package com.abtk.product.service.sys.service.impl;

import com.abtk.product.service.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典数据服务实现
 * TODO: 待后续集成sys_dict表后完善
 */
@Slf4j
@Service
public class DictServiceImpl implements DictService {

    @Override
    public List<Map<String, Object>> getDictData(String dictType) {
        log.info("查询字典数据, dictType={}", dictType);
        // TODO: 待集成sys_dict表后，从数据库查询
        // 当前返回占位数据
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("label", "示例选项");
        item.put("value", "1");
        result.add(item);
        return result;
    }

    @Override
    public String getDictLabel(String dictType, String dictValue) {
        log.info("查询字典标签, dictType={}, dictValue={}", dictType, dictValue);
        // TODO: 待集成sys_dict表后，从数据库查询
        return "示例标签";
    }

    @Override
    public Map<String, List<Map<String, Object>>> getDictDataBatch(List<String> dictTypes) {
        log.info("批量查询字典数据, dictTypes={}", dictTypes);
        // TODO: 待集成sys_dict表后，从数据库查询
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        for (String dictType : dictTypes) {
            result.put(dictType, getDictData(dictType));
        }
        return result;
    }
}
