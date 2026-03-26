package com.abclonal.product.web.controller.sys;

import com.abclonal.product.api.domain.request.dict.WmsDictRequest;
import com.abclonal.product.api.domain.response.dict.WmsDictResponse;
import com.abclonal.product.biz.dict.WmsDictBiz;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.abclonal.product.common.utils.PageUtils.startPage;

/**
 * 字典档案Controller（/api/base/dict）
 *
 * @author backend
 * @since 2026-03-26
 */
@RestController
@RequestMapping("/api/base/dict")
public class WmsDictController extends BaseController {

    @Autowired
    private WmsDictBiz wmsDictBiz;

    /**
     * 分页查询字典列表
     */
    @RequiresPermissions("wms:base:dict:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(WmsDictRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(wmsDictBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有字典（不分页）
     */
    @RequiresPermissions("wms:base:dict:list")
    @GetMapping("/listAll")
    public R<List<WmsDictResponse>> listAll() {
        return wmsDictBiz.listAll();
    }

    /**
     * 根据ID查询字典详情
     */
    @RequiresPermissions("wms:base:dict:list")
    @GetMapping("/{id}")
    public R<WmsDictResponse> getById(@PathVariable("id") Long id) {
        return wmsDictBiz.queryById(id);
    }

    /**
     * 新增字典
     */
    @Log(title = "字典档案", businessType = BusinessType.INSERT)
    @RequiresPermissions("wms:base:dict:add")
    @PostMapping
    public R<Long> create(@Valid @RequestBody WmsDictRequest request) {
        return wmsDictBiz.add(request);
    }

    /**
     * 编辑字典
     */
    @Log(title = "字典档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:dict:edit")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable("id") Long id, @Valid @RequestBody WmsDictRequest request) {
        return wmsDictBiz.update(id, request);
    }

    /**
     * 删除字典
     */
    @Log(title = "字典档案", businessType = BusinessType.DELETE)
    @RequiresPermissions("wms:base:dict:delete")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        return wmsDictBiz.delete(id);
    }

    /**
     * 切换字典状态
     */
    @Log(title = "字典档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:dict:edit")
    @PatchMapping("/{id}/status")
    public R<Void> toggleStatus(@PathVariable("id") Long id, @RequestParam("enabled") Integer enabled) {
        return wmsDictBiz.toggleStatus(id, enabled);
    }
}
