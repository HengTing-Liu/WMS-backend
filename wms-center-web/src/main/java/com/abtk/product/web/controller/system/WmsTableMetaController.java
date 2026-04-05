package com.abtk.product.web.controller.system;

import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.api.domain.request.sys.TableMetaRequest;
import com.abtk.product.api.domain.response.sys.TableMetaResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.WmsTableMeta;
import com.abtk.product.domain.converter.TableMetaConverter;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.sys.service.IWmsTableMetaService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表元数据管理控制器
 *
 * @author backend
 * @since 2026-04-06
 */
@Tag(name = "表元数据管理", description = "表元数据相关接口")
@RestController
@RequestMapping("/api/system/meta/table")
public class WmsTableMetaController extends BaseController {

    @Autowired
    private IWmsTableMetaService wmsTableMetaService;

    /**
     * 查询表元数据列表
     */
    @Operation(summary = "查询表元数据列表", description = "支持分页和模糊搜索")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping
    public R<TableDataInfo> list(TableMetaQueryRequest queryRequest) {
        startPage();
        WmsTableMeta condition = new WmsTableMeta();
        BeanUtils.copyProperties(queryRequest, condition);
        List<WmsTableMeta> list = wmsTableMetaService.list(condition);
        // 转换为响应对象
        List<TableMetaResponse> responseList = list.stream()
                .map(TableMetaConverter.INSTANCE::entityToResponse)
                .collect(Collectors.toList());
        return R.ok(getDataTable(responseList));
    }

    /**
     * 根据ID查询表元数据详情
     */
    @Operation(summary = "查询表元数据详情", description = "根据ID获取表元数据详细信息")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/{id}")
    public R<TableMetaResponse> getInfo(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id) {
        WmsTableMeta tableMeta = wmsTableMetaService.getById(id);
        if (tableMeta == null) {
            return R.fail("表元数据不存在");
        }
        return R.ok(TableMetaConverter.INSTANCE.entityToResponse(tableMeta));
    }

    /**
     * 根据表编码查询表元数据
     */
    @Operation(summary = "根据编码查询", description = "根据表编码获取表元数据信息")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/code/{code}")
    public R<TableMetaResponse> getByCode(
            @Parameter(description = "表编码", required = true)
            @PathVariable String code) {
        WmsTableMeta tableMeta = wmsTableMetaService.getByTableCode(code);
        if (tableMeta == null) {
            return R.fail("表元数据不存在");
        }
        return R.ok(TableMetaConverter.INSTANCE.entityToResponse(tableMeta));
    }

    /**
     * 新增表元数据
     */
    @Operation(summary = "新增表元数据", description = "创建新的表元数据")
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Long> add(@Validated @RequestBody TableMetaRequest request) {
        WmsTableMeta tableMeta = new WmsTableMeta();
        BeanUtils.copyProperties(request, tableMeta);
        // 处理Boolean转Integer
        if (request.getIsTree() != null) {
            tableMeta.setIsTree(request.getIsTree() ? 1 : 0);
        }
        Long id = wmsTableMetaService.create(tableMeta);
        return R.ok(id, "新增成功");
    }

    /**
     * 更新表元数据
     */
    @Operation(summary = "更新表元数据", description = "根据ID更新表元数据信息")
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R<Void> edit(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id,
            @Validated @RequestBody TableMetaRequest request) {
        WmsTableMeta tableMeta = new WmsTableMeta();
        BeanUtils.copyProperties(request, tableMeta);
        // 处理Boolean转Integer
        if (request.getIsTree() != null) {
            tableMeta.setIsTree(request.getIsTree() ? 1 : 0);
        }
        wmsTableMetaService.update(id, tableMeta);
        return R.okMsg("更新成功");
    }

    /**
     * 删除表元数据
     */
    @Operation(summary = "删除表元数据", description = "根据ID删除表元数据（逻辑删除）")
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id) {
        wmsTableMetaService.delete(id);
        return R.okMsg("删除成功");
    }

    /**
     * 切换表元数据状态（启用/禁用）
     */
    @Operation(summary = "切换状态", description = "启用或禁用表元数据")
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/toggle")
    public R<Void> toggleStatus(
            @Parameter(description = "表元数据ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "状态（0-禁用，1-启用）", required = true)
            @RequestParam Integer status) {
        wmsTableMetaService.toggleStatus(id, status);
        return R.okMsg(status == 1 ? "启用成功" : "禁用成功");
    }

    /**
     * 查询所有表元数据（不分页）
     */
    @Operation(summary = "查询所有表元数据", description = "获取所有表元数据列表，不分页")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/all")
    public R<List<TableMetaResponse>> listAll() {
        List<WmsTableMeta> list = wmsTableMetaService.listAll();
        List<TableMetaResponse> responseList = list.stream()
                .map(TableMetaConverter.INSTANCE::entityToResponse)
                .collect(Collectors.toList());
        return R.ok(responseList);
    }

    /**
     * 根据模块查询表元数据
     */
    @Operation(summary = "根据模块查询", description = "根据所属模块查询表元数据列表")
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/module/{module}")
    public R<List<TableMetaResponse>> listByModule(
            @Parameter(description = "所属模块", required = true)
            @PathVariable String module) {
        List<WmsTableMeta> list = wmsTableMetaService.listByModule(module);
        List<TableMetaResponse> responseList = list.stream()
                .map(TableMetaConverter.INSTANCE::entityToResponse)
                .collect(Collectors.toList());
        return R.ok(responseList);
    }
}
