package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.WarehouseReceiverQueryRequest;
import com.abtk.product.api.domain.request.sys.WarehouseReceiverRequest;
import com.abtk.product.biz.sys.WarehouseReceiverBiz;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.service.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 仓库收货信息Controller
 */
@RestController
@RequestMapping("/api/base/warehouse/receiver")
public class WarehouseReceiverController extends BaseController {

    @Autowired
    private WarehouseReceiverBiz warehouseReceiverBiz;

    /**
     * 查询收货地址列表
     */
    @GetMapping("/list")
    public R list(WarehouseReceiverQueryRequest queryRequest) {
        return warehouseReceiverBiz.list(queryRequest);
    }

    /**
     * 根据ID查询收货地址详情
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return warehouseReceiverBiz.queryById(id);
    }

    /**
     * 新增收货地址
     */
    @Log(title = "仓库收货信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R create(@RequestBody @Valid WarehouseReceiverRequest request) {
        return warehouseReceiverBiz.add(request);
    }

    /**
     * 编辑收货地址
     */
    @Log(title = "仓库收货信息", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody @Valid WarehouseReceiverRequest request) {
        return warehouseReceiverBiz.update(id, request);
    }

    /**
     * 删除收货地址
     */
    @Log(title = "仓库收货信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return warehouseReceiverBiz.delete(id);
    }

    /**
     * 设为默认地址
     */
    @Log(title = "仓库收货信息", businessType = BusinessType.UPDATE)
    @PatchMapping("/{id}/default")
    public R setDefault(@PathVariable Long id) {
        return warehouseReceiverBiz.setDefault(id);
    }
}
