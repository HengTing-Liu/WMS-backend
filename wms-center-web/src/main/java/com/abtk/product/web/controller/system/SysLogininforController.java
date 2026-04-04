package com.abtk.product.web.controller.system;

import com.abtk.product.common.constant.CacheConstants;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.dao.entity.SysLogininfor;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.redis.service.RedisService;
import com.abtk.product.service.system.ISysLogininforService;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.web.security.annotation.InnerAuth;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统访问记录
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/log/in")
public class SysLogininforController extends BaseController
{
    @Autowired
    private ISysLogininforService logininforService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private RedisService redisService;

    @RequiresPermissions("system:logininfor:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysLogininfor logininfor)
    {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return R.ok(getDataTable(list));
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:logininfor:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogininfor logininfor)
    {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.login.log"));
    }

    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public R<String> remove(@PathVariable Long[] infoIds)
    {
        return R.ok(logininforService.deleteLogininforByIds(infoIds) > 0 ? "删除成功" : "删除失败");
    }

    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public R<String> clean()
    {
        logininforService.cleanLogininfor();
        return R.ok("清空成功");
    }

    @RequiresPermissions("system:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public R<String> unlock(@PathVariable("userName") String userName)
    {
        redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + userName);
        return R.ok("解锁成功");
    }

    @InnerAuth
    @PostMapping
    public R<Boolean> add(@RequestBody SysLogininfor logininfor)
    {
        return R.ok(logininforService.insertLogininfor(logininfor) > 0);
    }
}
