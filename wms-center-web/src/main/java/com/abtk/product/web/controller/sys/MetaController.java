package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.ColumnMetaRequest;
import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.api.domain.request.sys.TableMetaRequest;
import com.abtk.product.api.domain.response.sys.ColumnMetaVO;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.entity.TableOperation;
import com.abtk.product.service.sys.service.MetaService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 元数据管理Controller
 * 提供表元数据、字段元数据、操作按钮的查询和管理接口
 */
@Slf4j
@Tag(name = "元数据管理", description = "动态表单元数据管理接口")
@RestController
@RequestMapping("/api/system/meta")
public class MetaController extends BaseController {

    @Autowired
    private MetaService metaService;

    /**
     * 表元数据分页列表查询
     */
    @Operation(summary = "表元数据分页列表", description = "支持按表编码、表名称、模块模糊搜索")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/table")
    public R<?> listTableMeta(TableMetaQueryRequest queryRequest) {
        startPage();
        List<TableMeta> list = metaService.listPage(queryRequest);
        return R.ok(getDataTable(list));
    }

    /**
     * 获取表元数据（包含字段和操作按钮）
     */
    @Operation(summary = "获取表元数据", description = "根据表标识获取完整的表元数据，包含字段和操作按钮配置")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/table/{tableCode}")
    public R<TableMeta> getTableMeta(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode) {
        TableMeta tableMeta = metaService.getTableMeta(tableCode);
        return R.ok(tableMeta);
    }

    /**
     * 根据ID获取表元数据详情
     */
    @Operation(summary = "根据ID获取表元数据")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/table/id/{id}")
    public R<TableMeta> getTableMetaById(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id) {
        TableMeta tableMeta = metaService.getById(id);
        return R.ok(tableMeta);
    }

    /**
     * 根据编码获取表元数据详情
     */
    @Operation(summary = "根据编码获取表元数据")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/table/code/{code}")
    public R<TableMeta> getTableMetaByCode(
            @Parameter(description = "表编码", required = true)
            @PathVariable String code) {
        TableMeta tableMeta = metaService.getByCode(code);
        return R.ok(tableMeta);
    }

    /**
     * 查询所有表元数据列表
     */
    @Operation(summary = "查询所有表元数据", description = "获取所有启用的表元数据列表")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/table/list")
    public R<List<TableMeta>> listAllTables() {
        List<TableMeta> list = metaService.listAllTables();
        return R.ok(list);
    }

    /**
     * 根据模块查询表元数据
     */
    @Operation(summary = "按模块查询表元数据", description = "根据模块标识查询表元数据列表")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/table/listByModule")
    public R<List<TableMeta>> listTablesByModule(
            @Parameter(description = "模块标识(base/wms/sys)", required = true)
            @RequestParam String module) {
        List<TableMeta> list = metaService.listTablesByModule(module);
        return R.ok(list);
    }

    /**
     * 获取字段元数据列表
     */
    @Operation(summary = "获取字段元数据", description = "根据表标识获取字段元数据列表")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/column/list/{tableCode}")
    public R<List<ColumnMeta>> listColumnMeta(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode) {
        List<ColumnMeta> list = metaService.getColumnMetaList(tableCode);
        return R.ok(list);
    }

    /**
     * 获取字段 Schema（前端友好格式）
     */
    @Operation(summary = "获取字段 Schema", description = "根据表标识获取字段元数据的前端友好 Schema，低代码动态渲染专用，无需权限")
    @GetMapping("/column/schema")
    public R<List<ColumnMetaVO>> getColumnSchema(
            @Parameter(description = "表标识", required = true)
            @RequestParam String tableCode) {
        List<ColumnMetaVO> list = metaService.getColumnSchema(tableCode);
        return R.ok(list);
    }

    /**
     * 获取操作按钮列表
     */
    @Operation(summary = "获取操作按钮", description = "根据表标识获取操作按钮配置列表")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/operation/list/{tableCode}")
    public R<List<TableOperation>> listOperations(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode) {
        List<TableOperation> list = metaService.getOperationList(tableCode);
        return R.ok(list);
    }

    /**
     * 创建表元数据
     */
    @Operation(summary = "创建表元数据")
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping("/table")
    public R<Void> createTableMeta(@RequestBody @Valid TableMetaRequest request) {
        TableMeta tableMeta = new TableMeta();
        BeanUtils.copyProperties(request, tableMeta);
        metaService.saveTableMeta(tableMeta);
        return R.ok();
    }

    /**
     * 更新表元数据
     */
    @Operation(summary = "更新表元数据")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/table/{id}")
    public R<Void> updateTableMeta(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid TableMetaRequest request) {
        TableMeta tableMeta = new TableMeta();
        BeanUtils.copyProperties(request, tableMeta);
        tableMeta.setId(id);
        metaService.saveTableMeta(tableMeta);
        return R.ok();
    }

    /**
     * 删除表元数据
     */
    @Operation(summary = "删除表元数据", description = "根据ID删除表元数据（逻辑删除）")
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/table/{id}")
    public R<Void> deleteTableMeta(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id) {
        metaService.deleteTableMeta(id);
        return R.ok();
    }

    /**
     * 启用/禁用切换
     */
    @Operation(summary = "启用/禁用切换")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/table/{id}/toggle")
    public R<Void> toggleTableMeta(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id) {
        metaService.toggleStatus(id);
        return R.ok();
    }

    /**
     * 批量保存字段元数据
     */
    @Operation(summary = "保存字段元数据", description = "批量保存字段元数据配置")
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping("/column/save/{tableCode}")
    public R<Void> saveColumnMeta(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode,
            @RequestBody List<ColumnMetaRequest> requests) {
        List<ColumnMeta> columns = requests.stream().map(req -> {
            ColumnMeta column = new ColumnMeta();
            BeanUtils.copyProperties(req, column);
            return column;
        }).collect(Collectors.toList());

        metaService.saveColumnMetaList(tableCode, columns);
        return R.ok();
    }

    /**
     * 删除字段元数据
     */
    @Operation(summary = "删除字段元数据", description = "根据ID删除字段元数据（逻辑删除）")
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/column/{id}")
    public R<Void> deleteColumnMeta(
            @Parameter(description = "字段元数据ID", required = true)
            @PathVariable Long id) {
        metaService.deleteColumnMeta(id);
        return R.ok();
    }

    /**
     * 删除操作按钮
     */
    @Operation(summary = "删除操作按钮", description = "根据ID删除操作按钮配置（逻辑删除）")
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/operation/{id}")
    public R<Void> deleteOperation(
            @Parameter(description = "操作按钮ID", required = true)
            @PathVariable Long id) {
        metaService.deleteOperation(id);
        return R.ok();
    }
}
