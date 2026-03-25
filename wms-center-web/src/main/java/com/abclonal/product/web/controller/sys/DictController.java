package com.abclonal.product.web.controller.sys;

import com.abclonal.product.api.domain.response.sys.DictDataResponse;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.service.sys.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典数据查询Controller
 * 提供字典数据的查询接口，支持Select、Cascader等组件的字典数据源
 */
@Slf4j
@Tag(name = "字典数据", description = "字典数据查询接口")
@RestController
@RequestMapping("/api/system/dict")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    /**
     * 根据字典类型查询字典数据
     */
    @Operation(summary = "查询字典数据", description = "根据字典类型查询字典数据列表")
    @GetMapping("/data/{dictType}")
    public R<List<Map<String, Object>>> getDictData(
            @Parameter(description = "字典类型", required = true)
            @PathVariable String dictType) {
        List<Map<String, Object>> data = dictService.getDictData(dictType);
        return R.ok(data);
    }

    /**
     * 查询字典标签
     */
    @Operation(summary = "查询字典标签", description = "根据字典类型和值查询对应的标签")
    @GetMapping("/label")
    public R<String> getDictLabel(
            @Parameter(description = "字典类型", required = true)
            @RequestParam String dictType,
            @Parameter(description = "字典值", required = true)
            @RequestParam String dictValue) {
        String label = dictService.getDictLabel(dictType, dictValue);
        return R.ok(label);
    }

    /**
     * 批量查询字典数据
     */
    @Operation(summary = "批量查询字典数据", description = "根据多个字典类型批量查询字典数据")
    @PostMapping("/data/batch")
    public R<Map<String, List<Map<String, Object>>>> getDictDataBatch(
            @RequestBody List<String> dictTypes) {
        Map<String, List<Map<String, Object>>> data = dictService.getDictDataBatch(dictTypes);
        return R.ok(data);
    }
}
