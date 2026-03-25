package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.dao.entity.SysNotice;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.ISysNoticeService;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @RequiresPermissions("system:notice:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return R.ok(getDataTable(list));
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @RequiresPermissions("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public R<SysNotice> getInfo(@PathVariable Long noticeId)
    {
        return R.ok(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(SecurityUtils.getUsername());
        return R.ok(noticeService.insertNotice(notice) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改通知公告
     */
    @RequiresPermissions("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(noticeService.updateNotice(notice) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除通知公告
     */
    @RequiresPermissions("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public R<String> remove(@PathVariable Long[] noticeIds)
    {
        return R.ok(noticeService.deleteNoticeByIds(noticeIds) > 0 ? "删除成功" : "删除失败");
    }
}
