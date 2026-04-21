package com.abtk.product.web.controller.sys;

import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.DateUtils;
import com.abtk.product.common.utils.poi.DynamicExcelUtil;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.util.SqlInjectionValidator;
import com.abtk.product.service.sys.service.CrudService;
import com.abtk.product.service.sys.service.LowcodeTreeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import com.github.pagehelper.PageInfo;

/**
 * 低代码运行时 CRUD。不按表名配置细粒度权限（如 inv_warehouse:list），否则每表需在权限表同步一条；
 * 访问控制：需携带有效 Token（见 AuthInterceptor），业务可见性由菜单与路由控制。
 */
@Slf4j
@Tag(name = "低代码通用CRUD", description = "统一入口：列表/详情/新建/修改/删除/启用停用")
@RestController
@RequestMapping("/api/wms/crud")
public class LowcodeCrudController extends BaseController {

    @Autowired
    private CrudService crudService;

    @Autowired
    private LowcodeTreeService lowcodeTreeService;

    // ========== 列表查询 ==========

    @Operation(summary = "分页查询列表", description = "通用分页查询，支持权限过滤")
    @GetMapping("/{tableCode}/list")
    public R<TableDataInfo> list(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @RequestParam Map<String, Object> params,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        SqlInjectionValidator.validateTable(tableCode);
        TableDataInfo dataTable = crudService.list(tableCode, params, pageNum, pageSize);
        return R.ok(dataTable);
    }

    @Operation(summary = "查询所有（不分页）", description = "通用全量查询")
    @GetMapping("/{tableCode}/listAll")
    public R<List<Map<String, Object>>> listAll(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @RequestParam Map<String, Object> params) {
        SqlInjectionValidator.validateTable(tableCode);
        List<Map<String, Object>> list = crudService.listAll(tableCode, params);
        return R.ok(list);
    }

    // ========== 树形查询 ==========

    @Operation(summary = "树形分页查询", description = "分页查询指定父节点的子节点")
    @GetMapping("/{tableCode}/tree/list")
    public R<TableDataInfo> listTree(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "父节点列名", required = true) @RequestParam String parentColumn,
            @Parameter(description = "父节点值（null表示根节点）") @RequestParam(required = false) Long parentValue,
            @RequestParam Map<String, Object> params,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        TableDataInfo dataTable = lowcodeTreeService.listTree(tableCode, parentColumn, parentValue, params, pageNum, pageSize);
        return R.ok(dataTable);
    }

    @Operation(summary = "查询所有树节点（不分页）", description = "一次性加载所有节点，前端本地构建树")
    @GetMapping("/{tableCode}/tree/listAll")
    public R<List<Map<String, Object>>> listTreeAll(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "父节点列名", required = true) @RequestParam String parentColumn,
            @RequestParam Map<String, Object> params) {
        List<Map<String, Object>> list = lowcodeTreeService.listTreeAll(tableCode, parentColumn, params);
        return R.ok(list);
    }

    @Operation(summary = "查询子节点数量", description = "查询指定节点的直接子节点数量，可选按列值过滤")
    @GetMapping("/{tableCode}/tree/count")
    public R<Long> countChildren(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "父节点列名", required = true) @RequestParam String parentColumn,
            @Parameter(description = "父节点值", required = true) @RequestParam Long parentValue,
            @Parameter(description = "过滤列名（可选，例如 location_grade）")
            @RequestParam(required = false) String filterColumn,
            @Parameter(description = "过滤值列表（可选，与 filterColumn 配合使用，为 IN 查询）")
            @RequestParam(required = false) List<String> filterValues) {
        Long count = lowcodeTreeService.countChildren(tableCode, parentColumn, parentValue,
                filterColumn, filterValues);
        return R.ok(count);
    }

    @Operation(summary = "查询所有子孙节点ID", description = "递归查询所有子孙节点ID（不含自身，MySQL 8.0+）")
    @GetMapping("/{tableCode}/tree/descendantIds")
    public R<List<Long>> getDescendantIds(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "父节点列名", required = true) @RequestParam String parentColumn,
            @Parameter(description = "节点ID", required = true) @RequestParam Long nodeId) {
        List<Long> ids = lowcodeTreeService.getAllDescendantIds(tableCode, parentColumn, nodeId);
        return R.ok(ids);
    }

    // ========== 导出 ==========

    @Operation(summary = "导出数据", description = "通用Excel导出，根据字段配置动态生成表头")
    @GetMapping("/{tableCode}/export")
    public void export(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "查询参数") @RequestParam Map<String, Object> params,
            HttpServletResponse response) {
        SqlInjectionValidator.validateTable(tableCode);
        log.info("通用导出: tableCode={}", tableCode);

        // 获取导出数据
        Map<String, Object> exportData = crudService.exportList(tableCode, params);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) exportData.get("dataList");
        List<ColumnMeta> columns = (List<ColumnMeta>) exportData.get("columns");

        // 构建字段配置
        List<DynamicExcelUtil.ExportField> fields = new ArrayList<>();
        for (ColumnMeta col : columns) {
            DynamicExcelUtil.ExportField field = new DynamicExcelUtil.ExportField();
            field.setField(col.getField());
            field.setTitle(col.getTitle());
            field.setDataType(col.getDataType());
            field.setWidth(col.getWidth());
            fields.add(field);
        }

        // 生成文件名
        String fileName = tableCode + "_" + DateUtils.parseDateToStr("yyyyMMdd_HHmmss", new Date());

        // 导出Excel
        DynamicExcelUtil.exportExcel(response, dataList, fileName, fields);
    }

    // ========== 单条详情 ==========

    @Operation(summary = "查询详情", description = "根据ID查询单条记录")
    @GetMapping("/{tableCode}/{id}")
    public R<Map<String, Object>> getById(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true) @PathVariable Long id) {
        Map<String, Object> data = crudService.getById(tableCode, id);
        return R.ok(data);
    }

    // ========== 新增 ==========

    @Operation(summary = "新增记录", description = "新建数据记录")
    @PostMapping("/{tableCode}")
    public R<Long> create(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @RequestBody Map<String, Object> data) {
        Long id = crudService.create(tableCode, data);
        return R.ok(id);
    }

    // ========== 修改 ==========

    @Operation(summary = "修改记录", description = "更新数据记录")
    @PutMapping("/{tableCode}/{id}")
    public R<Void> update(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true) @PathVariable Long id,
            @RequestBody Map<String, Object> data) {
        crudService.update(tableCode, id, data);
        return R.ok();
    }

    // ========== 删除 ==========

    @Operation(summary = "删除记录", description = "逻辑删除数据记录")
    @DeleteMapping("/{tableCode}/{id}")
    public R<Void> delete(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true) @PathVariable Long id) {
        crudService.delete(tableCode, id);
        return R.ok();
    }

    @Operation(summary = "批量删除", description = "批量逻辑删除")
    @DeleteMapping("/{tableCode}")
    public R<Void> batchDelete(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @RequestBody List<Long> ids) {
        crudService.batchDelete(tableCode, ids);
        return R.ok();
    }

    @Operation(summary = "删除前检查占用", description = "检查记录是否被其他数据引用")
    @GetMapping("/{tableCode}/checkOccupancy/{id}")
    public R<Map<String, Object>> checkOccupancy(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true) @PathVariable Long id) {
        Map<String, Object> result = lowcodeTreeService.checkDeleteOccupancy(tableCode, id);
        return R.ok(result);
    }

    // ========== 启用/停用 ==========

    @Operation(summary = "启用/停用", description = "切换单条记录的状态")
    @PostMapping("/{tableCode}/toggle/{id}")
    public R<Void> toggle(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "记录ID", required = true) @PathVariable Long id,
            @Parameter(description = "目标状态（1=启用，0=停用）", required = true) @RequestParam Integer enabled) {
        lowcodeTreeService.toggleStatus(tableCode, id, enabled);
        return R.ok();
    }

    @Operation(summary = "批量启用/停用", description = "批量切换记录状态")
    @PostMapping("/{tableCode}/toggle")
    public R<Void> batchToggle(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "ID列表", required = true) @RequestBody List<Long> ids,
            @Parameter(description = "目标状态（1=启用，0=停用）", required = true) @RequestParam Integer enabled) {
        lowcodeTreeService.batchToggleStatus(tableCode, ids, enabled);
        return R.ok();
    }

    // ========== 校验 ==========

    @Operation(summary = "检查字段唯一性", description = "检查字段值是否唯一")
    @GetMapping("/{tableCode}/checkUnique")
    public R<Boolean> checkUnique(
            @Parameter(description = "表标识", required = true) @PathVariable String tableCode,
            @Parameter(description = "字段名", required = true) @RequestParam String field,
            @Parameter(description = "字段值", required = true) @RequestParam String value,
            @Parameter(description = "排除的ID（编辑时使用）") @RequestParam(required = false) Long excludeId) {
        boolean unique = crudService.checkUnique(tableCode, field, value, excludeId);
        return R.ok(unique);
    }
}