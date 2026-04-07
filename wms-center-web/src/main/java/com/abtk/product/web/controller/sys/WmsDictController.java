package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.dict.WmsDictRequest;
import com.abtk.product.api.domain.response.dict.WmsDictResponse;
import com.abtk.product.biz.dict.WmsDictBiz;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.abtk.product.common.utils.PageUtils.startPage;

/**
 * 瀛楀吀妗ｆController锛?api/base/dict锛?
 *
 * @author backend
 * @since 2026-03-26
 */
@RestController
@RequestMapping("/api/sys/dict")
public class WmsDictController extends BaseController {

    @Autowired
    private WmsDictBiz wmsDictBiz;

    /**
     * 鍒嗛〉鏌ヨ瀛楀吀鍒楄〃
     */
    @RequiresPermissions("wms:sys:dict:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(WmsDictRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(wmsDictBiz.list(queryRequest).getData()));
    }

    /**
     * 鏌ヨ鎵€鏈夊瓧鍏革紙涓嶅垎椤碉級
     */
    @RequiresPermissions("wms:sys:dict:list")
    @GetMapping("/listAll")
    public R<List<WmsDictResponse>> listAll() {
        return wmsDictBiz.listAll();
    }

    /**
     * 鏍规嵁ID鏌ヨ瀛楀吀璇︽儏
     */
    @RequiresPermissions("wms:sys:dict:list")
    @GetMapping("/{id}")
    public R<WmsDictResponse> getById(@PathVariable("id") Long id) {
        return wmsDictBiz.queryById(id);
    }

    /**
     * 鏂板瀛楀吀
     */
    @Log(title = "瀛楀吀妗ｆ", businessType = BusinessType.INSERT)
    @RequiresPermissions("wms:sys:dict:add")
    @PostMapping
    public R<Long> create(@Valid @RequestBody WmsDictRequest request) {
        return wmsDictBiz.add(request);
    }

    /**
     * 缂栬緫瀛楀吀
     */
    @Log(title = "瀛楀吀妗ｆ", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:sys:dict:edit")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable("id") Long id, @Valid @RequestBody WmsDictRequest request) {
        return wmsDictBiz.update(id, request);
    }

    /**
     * 鍒犻櫎瀛楀吀
     */
    @Log(title = "瀛楀吀妗ｆ", businessType = BusinessType.DELETE)
    @RequiresPermissions("wms:sys:dict:delete")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        return wmsDictBiz.delete(id);
    }

    /**
     * 鍒囨崲瀛楀吀鐘舵€?
     */
    @Log(title = "瀛楀吀妗ｆ", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:sys:dict:edit")
    @PatchMapping("/{id}/status")
    public R<Void> toggleStatus(@PathVariable("id") Long id, @RequestParam("enabled") Integer enabled) {
        return wmsDictBiz.toggleStatus(id, enabled);
    }
}

