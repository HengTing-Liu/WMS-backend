package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.common.text.Convert;
import com.abclonal.product.common.utils.DateUtils;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.common.utils.poi.ExcelUtil;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.domain.AjaxResult;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.service.domain.LoginUser;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.domain.vo.TreeSelect;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.ISysConfigService;
import com.abclonal.product.service.system.ISysPermissionService;
import com.abclonal.product.service.system.ISysUserService;
import com.abclonal.product.web.security.annotation.InnerAuth;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import com.abclonal.product.dao.entity.SysEnumItem;
import com.abclonal.product.api.domain.request.sys.SysEnumItemRequest;
import com.abclonal.product.biz.system.SysEnumItemBiz;
import com.abclonal.product.service.system.service.SysEnumItemService;
import com.abclonal.product.api.domain.response.sys.SysEnumItemResponse;
import com.abclonal.product.api.domain.request.sys.SysEnumItemBatchRequest;
import com.abclonal.product.service.system.service.I18nService;

/**
 * 枚举明细表(SysEnumItem)表控制层
 *
 * @author lht
 * @since 2026-03-06 16:22:22
 */
@RestController
@Slf4j
@RequestMapping("/api/enum/item")
public class SysEnumItemController  extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private SysEnumItemService sysEnumItemService;
    @Autowired
    private SysEnumItemBiz sysEnumItemBiz;

    @Autowired
    private I18nService i18nService;

    /**
     * 分页查询
     *
     * @param sysEnumItemRequest 筛选条件
     * @return 查询结果
     */
   @GetMapping("/list")
    public R<TableDataInfo<SysEnumItemResponse>> list(SysEnumItemRequest sysEnumItemRequest) {
         return R.ok(sysEnumItemBiz.list(sysEnumItemRequest));
        }
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R<SysEnumItemResponse> queryById(@PathVariable("id") Long id) {
        return  sysEnumItemBiz.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param sysEnumItem 实体
     * @return 新增结果
     */
    @PostMapping("/add")
    public R<SysEnumItemResponse> add(@RequestBody SysEnumItem sysEnumItem) {
        return sysEnumItemBiz.add(sysEnumItem);
    }
    /**
     * 编辑数据
     *
     * @param sysEnumItem 实体
     * @return 编辑结果
     */
    @PutMapping("/update")
    public R<Integer> edit(@RequestBody SysEnumItem sysEnumItem) {
        return R.ok(this.sysEnumItemService.update(sysEnumItem));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public R<Boolean> deleteById(@PathVariable("id") String id) {
        return R.ok(this.sysEnumItemService.logicDeleteById(Long.valueOf(id),SecurityUtils.getLoginUser().getUsername()));
    }
    /**
     * 删除数据
     *
     * @param ids 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteBatch")
    public R<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return sysEnumItemBiz.deleteBatch(ids);
    }
    /**
     * 批量新增数据
     *
     * @param sysEnumItemList
     * @return 新增结果
     */
    @PostMapping("/addBatch")
    public R<Boolean> addBatch(@RequestBody SysEnumItemBatchRequest sysEnumItemList) {
        return sysEnumItemBiz.addBatch(sysEnumItemList);
    }

    @Log(title = "sysEnumItem管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(
            summary = "导出",
            description = "根据查询导出"
    )
    public void export(HttpServletResponse response, @RequestBody SysEnumItem sysEnumItem) {
        log.info("user={}", JSON.toJSONString(sysEnumItem));
        List<SysEnumItem> list = sysEnumItemService.queryByCondition(sysEnumItem);
        ExcelUtil<SysEnumItem> util = new ExcelUtil<SysEnumItem>(SysEnumItem.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.enum.item"));
    }
    
    @Log(title = " SysEnumItem管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @Operation(
            summary = "导入新增",
            description = "导入新增"
    )
    public R<Boolean> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysEnumItemRequest> util = new ExcelUtil<SysEnumItemRequest>(SysEnumItemRequest.class);
        List<SysEnumItemRequest> sysEnumItemList = util.importExcel(file.getInputStream());
        SysEnumItemBatchRequest sysEnumItemBatchRequest = new SysEnumItemBatchRequest();
        sysEnumItemBatchRequest.setRecords(sysEnumItemList);
        return sysEnumItemBiz.addBatch(sysEnumItemBatchRequest);
    }

    @PostMapping("/downLoadTemplate")
    @Operation(
            summary = "下载模版",
            description = "下载模版"
    )
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysEnumItemRequest> util = new ExcelUtil<SysEnumItemRequest>(SysEnumItemRequest.class);
        util.importTemplateExcel(response, i18nService.getMessage("export.filename.enum.item"));
    }
    
 /**
     * 编辑数据
     *
     * @param sysEnumItemList
     * @return 编辑结果
     */
    @PutMapping("/updateBatch")
    public R<Boolean>  updateBatch(@RequestBody SysEnumItemBatchRequest sysEnumItemList) {
        return sysEnumItemBiz.updateBatch(sysEnumItemList);
    }
}

