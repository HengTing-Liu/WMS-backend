package com.abtk.product.web.controller.system;

import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.dao.entity.SysOperLog;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.system.ISysOperLogService;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.web.security.annotation.InnerAuth;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志记录
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/operlog")
public class SysOperlogController extends BaseController
{
    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private I18nService i18nService;

    @RequiresPermissions("system:operlog:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysOperLog operLog)
    {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return R.ok(getDataTable(list));
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:operlog:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog)
    {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.oper.log"));
    }

    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:operlog:remove")
    @DeleteMapping("/{operIds}")
    public R<String> remove(@PathVariable Long[] operIds)
    {
        return R.ok(operLogService.deleteOperLogByIds(operIds) > 0 ? "删除成功" : "删除失败");
    }

    @RequiresPermissions("system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<String> clean()
    {
        operLogService.cleanOperLog();
        return R.ok("清空成功");
    }

    @InnerAuth
    @PostMapping
    public R<Boolean> add(@RequestBody SysOperLog operLog)
    {
        return R.ok(operLogService.insertOperlog(operLog) > 0);
    }
}
