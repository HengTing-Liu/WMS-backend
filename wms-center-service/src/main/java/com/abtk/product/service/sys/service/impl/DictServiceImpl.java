package com.abtk.product.service.sys.service.impl;

import com.abtk.product.dao.entity.SysDictData;
import com.abtk.product.service.sys.service.DictService;
import com.abtk.product.service.system.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典数据服务实现
 * 对接 sys_dict_data 表，优先使用缓存
 */
@Slf4j
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Override
    public List<Map<String, Object>> getDictData(String dictType) {
        List<SysDictData> dictDatas = dictTypeService.selectDictDataByType(dictType);
        if (dictDatas == null) {
            return new ArrayList<>();
        }
        return dictDatas.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public String getDictLabel(String dictType, String dictValue) {
        List<SysDictData> dictDatas = dictTypeService.selectDictDataByType(dictType);
        if (dictDatas == null) {
            return null;
        }
        for (SysDictData data : dictDatas) {
            if (dictValue != null && dictValue.equals(data.getDictValue())) {
                return data.getDictLabel();
            }
        }
        return null;
    }

    @Override
    public Map<String, List<Map<String, Object>>> getDictDataBatch(List<String> dictTypes) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        for (String dictType : dictTypes) {
            result.put(dictType, getDictData(dictType));
        }
        return result;
    }

    private Map<String, Object> convertToMap(SysDictData data) {
        Map<String, Object> map = new HashMap<>();
        map.put("label", data.getDictLabel());
        map.put("value", data.getDictValue());
        map.put("id", data.getId());
        map.put("dictSort", data.getDictSort());
        map.put("isDefault", data.getIsDefault());
        map.put("status", data.getStatus());
        map.put("cssClass", data.getCssClass());
        map.put("listClass", data.getListClass());
        return map;
    }
}
