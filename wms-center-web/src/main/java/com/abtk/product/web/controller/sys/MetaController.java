package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.ColumnMetaRequest;
import com.abtk.product.api.domain.request.sys.FormGroupMetaRequest;
import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.api.domain.request.sys.TableMetaRequest;
import com.abtk.product.api.domain.response.sys.ColumnMetaVO;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.FormGroupMeta;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.entity.TableOperation;
import com.abtk.product.biz.sys.TableMetaBiz;
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
 */
@Slf4j
@Tag(name = "元数据管理", description = "动态表单元数据管理接口")
@RestController
@RequestMapping("/api/system/meta")
public class MetaController extends BaseController {

    @Autowired
    private MetaService metaService;

    @Autowired
    private TableMetaBiz tableMetaBiz;

    /**
     * 表元数据分页列表查询
     */
    @Operation(summary = "表元数据分页列表", description = "支持按表编码、表名称、模块查询")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/table")
    public R<?> listTableMeta(TableMetaQueryRequest queryRequest) {
        startPage();
        List<TableMeta> list = metaService.listPage(queryRequest);
        return R.ok(getDataTable(list));
    }

    /**
     * 获取表元数据（低代码页面运行时拉取，供 column/schema 及 operation/list 共用，只需登录）
     */
    @Operation(summary = "获取表元数据", description = "根据表标识获取完整的表元数据，包含字段和操作按钮配置")
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
     * 获取字段元数据详情
     */
    @Operation(summary = "获取字段元数据详情", description = "根据ID获取字段元数据详情")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/column/{id}")
    public R<ColumnMeta> getColumnMetaById(
            @Parameter(description = "字段元数据ID", required = true)
            @PathVariable Long id) {
        ColumnMeta data = metaService.getColumnMetaById(id);
        return R.ok(data);
    }

    /**
     * 表单分组列表（低代码树表/表单页运行时拉取，与 column/schema、operation/list 一致：仅需登录）
     */
    @Operation(summary = "获取表单分组元数据", description = "根据表标识获取表单分组元数据列表，低代码动态渲染专用")
    @GetMapping("/group/list/{tableCode}")
    public R<List<FormGroupMeta>> listFormGroupMeta(@PathVariable String tableCode) {
        return R.ok(metaService.getFormGroupMetaList(tableCode));
    }

    @Operation(summary = "获取表单分组元数据详情", description = "根据ID获取表单分组元数据详情")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/group/{id}")
    public R<FormGroupMeta> getFormGroupMetaById(@PathVariable Long id) {
        return R.ok(metaService.getFormGroupMetaById(id));
    }

    /**
     * 获取字段Schema（前端格式化）
     */
    @Operation(summary = "获取字段Schema", description = "根据表标识获取字段元数据的前端格式化Schema，低代码动态渲染专用，无需权限")
    @GetMapping("/column/schema")
    public R<List<ColumnMetaVO>> getColumnSchema(
            @Parameter(description = "表标识", required = true)
            @RequestParam String tableCode) {
        List<ColumnMetaVO> list = metaService.getColumnSchema(tableCode);
        return R.ok(list);
    }

    /**
     * 获取操作按钮列表（低代码动态渲染专用，无需权限）
     */
    @Operation(summary = "获取操作按钮", description = "根据表标识获取操作按钮配置列表")
    @GetMapping("/operation/list/{tableCode}")
    public R<TableDataInfo<TableOperation>> listOperations(
            @Parameter(description = "表标识", required = true)
            @PathVariable String tableCode) {
        List<TableOperation> list = metaService.getOperationList(tableCode);
        TableDataInfo<TableOperation> tableData = new TableDataInfo<>();
        tableData.setRows(list);
        tableData.setTotal(list.size());
        return R.ok(tableData);
    }

    /**
     * 获取操作按钮详情
     */
    @Operation(summary = "获取操作按钮详情", description = "根据ID获取操作按钮详情")
    @RequiresPermissions("system:meta:query")
    @GetMapping("/operation/{id}")
    public R<TableOperation> getOperation(
            @Parameter(description = "操作按钮ID", required = true)
            @PathVariable Long id) {
        TableOperation operation = metaService.getOperationById(id);
        return R.ok(operation);
    }

    /**
     * 保存操作按钮
     */
    @Operation(summary = "保存操作按钮", description = "新增或更新操作按钮配置")
    @RequiresPermissions("system:meta:edit")
    @PostMapping("/operation")
    public R<TableOperation> saveOperation(@RequestBody TableOperation operation) {
        TableOperation result = metaService.saveOperation(operation);
        return R.ok(result);
    }

    /**
     * 更新操作按钮
     */
    @Operation(summary = "更新操作按钮", description = "更新操作按钮配置")
    @RequiresPermissions("system:meta:edit")
    @PutMapping("/operation/{id}")
    public R<TableOperation> updateOperation(
            @PathVariable Long id,
            @RequestBody TableOperation operation) {
        operation.setId(id);
        TableOperation result = metaService.saveOperation(operation);
        return R.ok(result);
    }

    /**
     * 批量更新操作按钮排序
     */
    @Operation(summary = "更新操作按钮排序", description = "批量更新操作按钮排序序号")
    @RequiresPermissions("system:meta:edit")
    @PutMapping("/operation/sort")
    public R<Void> sortOperations(@RequestBody List<TableOperation> operations) {
        metaService.sortOperations(operations);
        return R.ok();
    }

    /**
     * 创建表元数据（走 biz 层，自动注入 BaseEntity 固定字段）
     */
    @Operation(summary = "创建表元数据")
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping("/table")
    public R<Long> createTableMeta(@RequestBody @Valid TableMetaRequest request) {
        R<Long> result = tableMetaBiz.add(request);
        if (result.getCode() != 200) {
            return result;
        }
        return result;
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
    @Operation(summary = "删除表元数据", description = "根据ID删除表元数据（级联删除）")
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
     * 批量更新字段排序序号
     */
    @Operation(summary = "批量更新字段排序序号")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/column/sort")
    public R<Void> batchUpdateColumnSort(@RequestBody List<ColumnMetaRequest> requests) {
        List<ColumnMeta> columns = requests.stream().map(req -> {
            ColumnMeta column = new ColumnMeta();
            BeanUtils.copyProperties(req, column);
            return column;
        }).collect(Collectors.toList());
        metaService.batchUpdateColumnSort(columns);
        return R.ok();
    }

    /**
     * 保存字段元数据
     */
    @Operation(summary = "保存字段元数据")
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
    @Operation(summary = "删除字段元数据", description = "根据ID删除字段元数据（级联删除）")
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/column/{id}")
    public R<Void> deleteColumnMeta(
            @Parameter(description = "字段元数据ID", required = true)
            @PathVariable Long id) {
        metaService.deleteColumnMeta(id);
        return R.ok();
    }

    /**
     * 新增字段元数据
     */
    @Operation(summary = "新增字段元数据")
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping("/column")
    public R<Void> addColumnMeta(@RequestBody ColumnMetaRequest request) {
        ColumnMeta column = new ColumnMeta();
        BeanUtils.copyProperties(request, column);
        metaService.addColumnMeta(column);
        return R.ok();
    }

    /**
     * 批量新增字段元数据（非破坏性，逐条 addColumnMeta）
     * <p>WMS-LOWCODE-LOOKUP 批量添加关联字段向导使用。</p>
     * <p>与 {@link #saveColumnMeta} 区别：batch-insert 不会删除已有字段，只做追加。</p>
     */
    @Operation(summary = "批量新增字段元数据（追加）")
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping("/column/batch-insert")
    public R<Void> batchInsertColumnMeta(@RequestBody java.util.List<ColumnMetaRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return R.ok();
        }
        for (ColumnMetaRequest request : requests) {
            ColumnMeta column = new ColumnMeta();
            BeanUtils.copyProperties(request, column);
            metaService.addColumnMeta(column);
        }
        return R.ok();
    }

    /**
     * 更新字段元数据
     */
    @Operation(summary = "更新字段元数据")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/column/{id}")
    public R<Void> updateColumnMeta(
            @PathVariable Long id,
            @RequestBody ColumnMetaRequest request) {
        ColumnMeta column = new ColumnMeta();
        BeanUtils.copyProperties(request, column);
        column.setId(id);
        metaService.updateColumnMeta(column);
        return R.ok();
    }

    @Operation(summary = "新增表单分组元数据")
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping("/group")
    public R<FormGroupMeta> addFormGroupMeta(@RequestBody FormGroupMetaRequest request) {
        FormGroupMeta formGroupMeta = new FormGroupMeta();
        BeanUtils.copyProperties(request, formGroupMeta);
        return R.ok(metaService.saveFormGroupMeta(formGroupMeta));
    }

    @Operation(summary = "批量更新字段栅格列宽")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/column/colspan")
    public R<Void> batchUpdateColumnColSpan(@RequestBody ColumnMetaRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) {
            return R.ok();
        }
        metaService.batchUpdateColumnColSpan(request.getIds(), request.getColSpan());
        return R.ok();
    }

    @Operation(summary = "批量更新字段分组信息")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/column/batch-group")
    public R<Void> batchUpdateColumnGroup(@RequestBody ColumnMetaRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) {
            return R.ok();
        }
        metaService.batchUpdateColumnSection(
                request.getIds(),
                request.getSectionKey(),
                request.getSectionTitle(),
                request.getSectionOrder(),
                request.getSectionType(),
                request.getSectionOpen()
        );
        return R.ok();
    }

    @Operation(summary = "更新表单分组元数据")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/group/{id}")
    public R<FormGroupMeta> updateFormGroupMeta(
            @PathVariable Long id,
            @RequestBody FormGroupMetaRequest request) {
        FormGroupMeta formGroupMeta = new FormGroupMeta();
        BeanUtils.copyProperties(request, formGroupMeta);
        formGroupMeta.setId(id);
        return R.ok(metaService.saveFormGroupMeta(formGroupMeta));
    }

    @Operation(summary = "批量更新表单分组排序")
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/group/sort")
    public R<Void> batchUpdateFormGroupSort(@RequestBody List<FormGroupMetaRequest> requests) {
        List<FormGroupMeta> groups = requests.stream().map(req -> {
            FormGroupMeta group = new FormGroupMeta();
            BeanUtils.copyProperties(req, group);
            return group;
        }).collect(Collectors.toList());
        metaService.batchUpdateFormGroupSort(groups);
        return R.ok();
    }

    @Operation(summary = "删除表单分组元数据")
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/group/{id}")
    public R<Void> deleteFormGroupMeta(@PathVariable Long id) {
        metaService.deleteFormGroupMeta(id);
        return R.ok();
    }

    /**
     * 删除操作按钮
     */
    @Operation(summary = "删除操作按钮", description = "根据ID删除操作按钮配置（级联删除）")
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/operation/{id}")
    public R<Void> deleteOperation(
            @Parameter(description = "操作按钮ID", required = true)
            @PathVariable Long id) {
        metaService.deleteOperation(id);
        return R.ok();
    }

    /**
     * 批量删除操作按钮
     */
    @Operation(summary = "批量删除操作按钮", description = "批量删除操作按钮配置")
    @RequiresPermissions("system:meta:delete")
    @DeleteMapping("/operation")
    public R<Void> deleteOperations(@RequestBody List<Long> ids) {
        metaService.deleteOperations(ids);
        return R.ok();
    }
}
