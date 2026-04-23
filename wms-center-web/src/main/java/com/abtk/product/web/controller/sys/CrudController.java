package com.abtk.product.web.controller.sys;

import com.abtk.product.biz.system.CrudSerialNumberBiz;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.sys.service.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通用CRUD Controller（/api/crud）。与 {@link LowcodeCrudController} 相同策略：不按表名配置细粒度权限，需有效 Token。
 */
@Slf4j
@Tag(name = "通用CRUD", description = "基于元数据配置的动态CRUD接口")
@RestController
@RequestMapping("/api/crud")
public class CrudController extends BaseController {

    @Autowired
    private CrudService crudService;

    @Autowired
    private CrudSerialNumberBiz crudSerialNumberBiz;

    /**
     * 分页查询列表
     */
    @Operation(summary = "分页查询", description = "根据表标识分页查询数据列表")
    @GetMapping("/{tableCode}/list")
    public R<TableDataInfo> list(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @RequestParam Map<String, Object> params,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        TableDataInfo dataTable = crudService.list(tableCode, params, pageNum, pageSize);
        return R.ok(dataTable);
    }

    /**
     * 查询所有数据（不分页）
     */
    @Operation(summary = "查询所有", description = "根据表标识查询所有数据（不分页）")
    @GetMapping("/{tableCode}/listAll")
    public R<List<Map<String, Object>>> listAll(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @RequestParam Map<String, Object> params) {
        List<Map<String, Object>> list = crudService.listAll(tableCode, params);
        return R.ok(list);
    }

    /**
     * 根据ID查询详情
     */
    @Operation(summary = "查询详情", description = "根据表标识和ID查询数据详情")
    @GetMapping("/{tableCode}/{id}")
    public R<Map<String, Object>> getById(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true)
            @PathVariable Long id) {
        Map<String, Object> data = crudService.getById(tableCode, id);
        return R.ok(data);
    }

    /**
     * 新增记录
     */
    @Operation(summary = "新增记录", description = "根据表标识新增数据记录")
    @PostMapping("/{tableCode}")
    public R<Long> create(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @RequestBody Map<String, Object> data) {
        crudSerialNumberBiz.fillSerialNumbers(tableCode, data);
        Long id = crudService.create(tableCode, data);
        return R.ok(id);
    }

    /**
     * 更新记录
     */
    @Operation(summary = "更新记录", description = "根据表标识和ID更新数据记录")
    @PutMapping("/{tableCode}/{id}")
    public R<Void> update(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true)
            @PathVariable Long id,
            @RequestBody Map<String, Object> data) {
        crudService.update(tableCode, id, data);
        return R.ok();
    }

    /**
     * 删除记录
     */
    @Operation(summary = "删除记录", description = "根据表标识和ID删除数据记录（逻辑删除）")
    @DeleteMapping("/{tableCode}/{id}")
    public R<Void> delete(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true)
            @PathVariable Long id) {
        crudService.delete(tableCode, id);
        return R.ok();
    }

    /**
     * 批量删除
     */
    @Operation(summary = "批量删除", description = "根据表标识批量删除数据记录（逻辑删除）")
    @DeleteMapping("/{tableCode}")
    public R<Void> batchDelete(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @RequestBody List<Long> ids) {
        crudService.batchDelete(tableCode, ids);
        return R.ok();
    }

    /**
     * 检查字段唯一性
     */
    @Operation(summary = "检查唯一性", description = "检查指定字段值是否唯一")
    @GetMapping("/{tableCode}/checkUnique")
    public R<Boolean> checkUnique(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @Parameter(description = "字段名", required = true)
            @RequestParam String field,
            @Parameter(description = "字段值", required = true)
            @RequestParam String value,
            @Parameter(description = "排除的ID（编辑时使用）")
            @RequestParam(required = false) Long excludeId) {
        boolean unique = crudService.checkUnique(tableCode, field, value, excludeId);
        return R.ok(unique);
    }
}
