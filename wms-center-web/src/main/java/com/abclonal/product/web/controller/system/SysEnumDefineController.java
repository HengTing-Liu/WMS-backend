package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.common.utils.poi.ExcelUtil;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.dao.entity.SysEnumDefine;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.service.SysEnumDefineService;
import com.abclonal.product.biz.system.SysEnumDefineBiz;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.abclonal.product.api.domain.request.sys.SysEnumDefineRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineResponse;
import com.abclonal.product.api.domain.request.sys.SysEnumDefineBatchRequest;
import com.abclonal.product.api.domain.request.sys.SysEnumDefineWithItemsRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineWithItemsResponse;
import com.abclonal.product.service.system.service.I18nService;

/**
 * 枚举定义表(SysEnumDefine)表控制层
 *
 * @author lht
 * @since 2026-03-06 14:39:38
 */
@RestController
@Slf4j
@RequestMapping("/api/enum/define")
public class SysEnumDefineController extends BaseController {

    @Autowired
    private SysEnumDefineService sysEnumDefineService;

    @Autowired
    private SysEnumDefineBiz sysEnumDefineBiz;

    @Autowired
    private I18nService i18nService;

    /**
     * 分页查询
     */
    @GetMapping("/list")
    public R<TableDataInfo<SysEnumDefineResponse>> list(SysEnumDefineRequest sysEnumDefineRequest) {
        return R.ok(sysEnumDefineBiz.list(sysEnumDefineRequest));
    }

    /**
     * 通过主键查询单条数据
     */
    @GetMapping("{id}")
    public R<SysEnumDefineResponse> queryById(@PathVariable("id") Long id) {
        return sysEnumDefineBiz.queryById(id);
    }

    /**
     * 新增数据
     */
    @PostMapping("/add")
    public R<SysEnumDefineResponse> add(@RequestBody SysEnumDefine sysEnumDefine) {
        return sysEnumDefineBiz.add(sysEnumDefine);
    }

    /**
     * 新增枚举定义及明细
     */
    @PostMapping("/addWithItems")
    @Operation(
            summary = "新增枚举定义及明细",
            description = "同时新增枚举定义和对应的枚举明细"
    )
    public R<SysEnumDefineWithItemsResponse> addWithItems(@RequestBody SysEnumDefineWithItemsRequest request) {
        return sysEnumDefineBiz.addWithItems(request);
    }

    /**
     * 编辑数据
     */
    @PutMapping("/update")
    public R<Integer> edit(@RequestBody SysEnumDefine sysEnumDefine) {
        return R.ok(this.sysEnumDefineService.update(sysEnumDefine));
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/{id}")
    public R<Boolean> deleteById(@PathVariable("id") String id) {
        return R.ok(this.sysEnumDefineService.logicDeleteById(Long.valueOf(id), SecurityUtils.getLoginUser().getUsername()));
    }

    /**
     * 批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public R<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return sysEnumDefineBiz.deleteBatch(ids);
    }

    /**
     * 批量新增数据
     */
    @PostMapping("/addBatch")
    public R<Boolean> addBatch(@RequestBody SysEnumDefineBatchRequest sysEnumDefineList) {
        return sysEnumDefineBiz.addBatch(sysEnumDefineList);
    }

    /**
     * 导出数据
     */
    @Log(title = "sysEnumDefine管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(summary = "导出", description = "根据查询导出")
    public void export(HttpServletResponse response, @RequestBody SysEnumDefine sysEnumDefine ) {
        log.info("user={}", com.alibaba.fastjson.JSON.toJSONString(sysEnumDefine));
        List<SysEnumDefine> list = sysEnumDefineService.queryByCondition(sysEnumDefine);
        ExcelUtil<SysEnumDefine> util = new ExcelUtil<SysEnumDefine>(SysEnumDefine.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.enum.define"));
    }

    /**
     * 导入数据
     */
    @Log(title = "SysEnumDefine管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @Operation(summary = "导入新增", description = "导入新增")
    public R<Boolean> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysEnumDefineRequest> util = new ExcelUtil<SysEnumDefineRequest>(SysEnumDefineRequest.class);
        List<SysEnumDefineRequest> sysEnumDefineList = util.importExcel(file.getInputStream());
        SysEnumDefineBatchRequest sysEnumDefineBatchRequest = new SysEnumDefineBatchRequest();
        sysEnumDefineBatchRequest.setRecords(sysEnumDefineList);
        return sysEnumDefineBiz.addBatch(sysEnumDefineBatchRequest);
    }

    /**
     * 下载模板
     */
    @PostMapping("/downLoadTemplate")
    @Operation(summary = "下载模版", description = "下载模版")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysEnumDefineRequest> util = new ExcelUtil<SysEnumDefineRequest>(SysEnumDefineRequest.class);
        util.importTemplateExcel(response, i18nService.getMessage("export.filename.enum.define"));
    }

    /**
     * 批量编辑数据
     */
    @PutMapping("/updateBatch")
    public R<Boolean> updateBatch(@RequestBody SysEnumDefineBatchRequest sysEnumDefineList) {
        return sysEnumDefineBiz.updateBatch(sysEnumDefineList);
    }

}
