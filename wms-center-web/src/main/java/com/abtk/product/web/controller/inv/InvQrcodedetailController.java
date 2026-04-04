package com.abtk.product.web.controller.inv;

import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import com.abtk.product.dao.entity.InvQrcodedetail;
import com.abtk.product.api.domain.request.inv.InvQrcodedetailRequest;
import com.abtk.product.biz.inv.InvQrcodedetailBiz;
import com.abtk.product.service.inv.service.InvQrcodedetailService;
import com.abtk.product.api.domain.response.inv.InvQrcodedetailResponse;
import com.abtk.product.api.domain.request.inv.InvQrcodedetailBatchRequest;
import com.abtk.product.service.system.service.I18nService;

/**
 * (InvQrcodedetail)表控制层
 *
 * @author lht
 * @since 2026-03-04 11:03:22
 */
@RestController
@Slf4j
@RequestMapping("invQrcodedetail")
public class InvQrcodedetailController  extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private InvQrcodedetailService invQrcodedetailService;
    @Autowired
    private InvQrcodedetailBiz invQrcodedetailBiz;

    @Autowired
    private I18nService i18nService;

    /**
     * 分页查询
     *
     * @param invQrcodedetailRequest 筛选条件
     * @return 查询结果
     */
   @GetMapping("/list")
    public R<TableDataInfo<InvQrcodedetailResponse>> list(InvQrcodedetailRequest invQrcodedetailRequest) {
         return  R.ok(invQrcodedetailBiz.list(invQrcodedetailRequest));
        }
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R<InvQrcodedetailResponse> queryById(@PathVariable("id") Long id) {
        return  invQrcodedetailBiz.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param invQrcodedetail 实体
     * @return 新增结果
     */
    @PostMapping("/add")
    public R<InvQrcodedetailResponse> add(@RequestBody InvQrcodedetail invQrcodedetail) {
        return invQrcodedetailBiz.add(invQrcodedetail);
    }
    /**
     * 编辑数据
     *
     * @param invQrcodedetail 实体
     * @return 编辑结果
     */
    @PutMapping("/update")
    public R<Integer> edit(@RequestBody InvQrcodedetail invQrcodedetail) {
        return R.ok(this.invQrcodedetailService.update(invQrcodedetail));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteById")
    public R<Boolean> deleteById(Long id) {
        return R.ok(this.invQrcodedetailService.logicDeleteById(id,SecurityUtils.getLoginUser().getUsername()));
    }
    /**
     * 删除数据
     *
     * @param ids 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteBatch")
    public R<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return invQrcodedetailBiz.deleteBatch(ids);
    }
    /**
     * 批量新增数据
     *
     * @param invQrcodedetailList
     * @return 新增结果
     */
    @PostMapping("/addBatch")
    public R<Boolean> addBatch(@RequestBody InvQrcodedetailBatchRequest invQrcodedetailList) {
        return invQrcodedetailBiz.addBatch(invQrcodedetailList);
    }

    @Log(title = "invQrcodedetail管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @Operation(
            summary = "导出",
            description = "根据查询导出"
    )
    public void export(HttpServletResponse response, @RequestBody InvQrcodedetail invQrcodedetail) {
        log.info("user={}", JSON.toJSONString(invQrcodedetail));
        List<InvQrcodedetail> list = invQrcodedetailService.queryByCondition(invQrcodedetail);
        ExcelUtil<InvQrcodedetail> util = new ExcelUtil<InvQrcodedetail>(InvQrcodedetail.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.inv.qrcodedetail"));
    }
    
    @Log(title = " InvQrcodedetail管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @Operation(
            summary = "导入新增",
            description = "导入新增"
    )
    public R<Boolean> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<InvQrcodedetailRequest> util = new ExcelUtil<InvQrcodedetailRequest>(InvQrcodedetailRequest.class);
        List<InvQrcodedetailRequest> invQrcodedetailList = util.importExcel(file.getInputStream());
        InvQrcodedetailBatchRequest invQrcodedetailBatchRequest = new InvQrcodedetailBatchRequest();
        invQrcodedetailBatchRequest.setRecords(invQrcodedetailList);
        return invQrcodedetailBiz.addBatch(invQrcodedetailBatchRequest);
    }

    @PostMapping("/downLoadTemplate")
    @Operation(
            summary = "下载模版",
            description = "下载模版"
    )
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<InvQrcodedetailRequest> util = new ExcelUtil<InvQrcodedetailRequest>(InvQrcodedetailRequest.class);
        util.importTemplateExcel(response, i18nService.getMessage("export.filename.inv.qrcodedetail"));
    }
    
 /**
     * 编辑数据
     *
     * @param invQrcodedetailList
     * @return 编辑结果
     */
    @PutMapping("/updateBatch")
    public R<Boolean>  updateBatch(@RequestBody InvQrcodedetailBatchRequest invQrcodedetailList) {
        return invQrcodedetailBiz.updateBatch(invQrcodedetailList);
    }
}

