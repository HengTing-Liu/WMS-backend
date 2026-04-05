package com.abtk.product.web.controller.system;

import com.abtk.product.api.domain.request.system.WmsTableMetaRequest;
import com.abtk.product.api.domain.response.system.WmsTableMetaResponse;
import com.abtk.product.biz.system.WmsTableMetaBiz;
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
 * hCpn°Controller/api/system/meta/table	
 *
 * @author backend
 * @since 2026-04-06
 */
@RestController
@RequestMapping("/api/system/meta/table")
public class WmsTableMetaController extends BaseController {

    @Autowired
    private WmsTableMetaBiz wmsTableMetaBiz;

    /**
     * uÂ‚hCpnh
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping
    public R<TableDataInfo> list(WmsTableMetaRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(wmsTableMetaBiz.list(queryRequest).getData()));
    }

    /**
     * Â‚@	hCpnu	
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/listAll")
    public R<List<WmsTableMetaResponse>> listAll() {
        return wmsTableMetaBiz.listAll();
    }

    /**
     * 9nIDÂ‚hCpnÊ≈
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/{id}")
    public R<WmsTableMetaResponse> getById(@PathVariable("id") Long id) {
        return wmsTableMetaBiz.queryById(id);
    }

    /**
     * 9nÂ‚hCpn
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/code/{code}")
    public R<WmsTableMetaResponse> getByCode(@PathVariable("code") String code) {
        return wmsTableMetaBiz.queryByCode(code);
    }

    /**
     * ∞ûhCpn
     */
    @Log(title = "hCpn°", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping
    public R<Long> create(@Valid @RequestBody WmsTableMetaRequest request) {
        return wmsTableMetaBiz.add(request);
    }

    /**
     * ëhCpn
     */
    @Log(title = "hCpn°", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable("id") Long id, @Valid @RequestBody WmsTableMetaRequest request) {
        return wmsTableMetaBiz.update(id, request);
    }

    /**
     *  dhCpn
     */
    @Log(title = "hCpn°", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        return wmsTableMetaBiz.delete(id);
    }

    /**
     * bhCpn∂/(/Å(	
     */
    @Log(title = "hCpn°", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/{id}/toggle")
    public R<Void> toggleStatus(@PathVariable("id") Long id) {
        return wmsTableMetaBiz.toggleStatus(id);
    }
}
