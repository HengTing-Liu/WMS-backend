package com.abclonal.product.web.controller.sys;

import com.abclonal.product.api.domain.request.location.WmsLocationRequest;
import com.abclonal.product.api.domain.response.location.WmsLocationResponse;
import com.abclonal.product.biz.sys.LocationBiz;
import com.abclonal.product.dao.entity.WmsLocation;
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

/**
 * 库位档案Controller（/api/base/location）
 *
 * @author backend
 * @since 2026-03-26
 */
@RestController
@RequestMapping("/api/base/location")
public class LocationController extends BaseController {

    @Autowired
    private LocationBiz locationBiz;

    /**
     * 分页查询库位列表
     */
    @RequiresPermissions("base:location:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(WmsLocationRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(locationBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有库位（不分页）
     */
    @RequiresPermissions("base:location:list")
    @GetMapping("/listAll")
    public R<List<WmsLocation>> listAll() {
        return locationBiz.listAll();
    }

    /**
     * 根据ID查询库位详情
     */
    @RequiresPermissions("base:location:list")
    @GetMapping("/{id}")
    public R<WmsLocationResponse> getById(@PathVariable("id") Long id) {
        return locationBiz.queryById(id);
    }

    /**
     * 新增库位
     */
    @Log(title = "库位档案", businessType = BusinessType.INSERT)
    @RequiresPermissions("base:location:add")
    @PostMapping
    public R<Long> create(@Valid @RequestBody WmsLocationRequest request) {
        return locationBiz.add(request);
    }

    /**
     * 编辑库位
     */
    @Log(title = "库位档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("base:location:edit")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable("id") Long id, @Valid @RequestBody WmsLocationRequest request) {
        return locationBiz.update(id, request);
    }

    /**
     * 删除库位
     */
    @Log(title = "库位档案", businessType = BusinessType.DELETE)
    @RequiresPermissions("base:location:delete")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        return locationBiz.delete(id);
    }

    /**
     * 切换库位状态
     */
    @Log(title = "库位档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("base:location:edit")
    @PatchMapping("/{id}/status")
    public R<Void> toggleStatus(@PathVariable("id") Long id, @RequestParam("enabled") Integer enabled) {
        return locationBiz.toggleStatus(id, enabled);
    }
}
