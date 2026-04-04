package com.abtk.product.web.controller.system;

import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.domain.AjaxResult;
import com.abtk.product.service.sys.cache.MetaCacheService;
import com.abtk.product.service.sys.cache.dto.CacheStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 元数据缓存管理 Controller
 * <p>
 * 提供缓存的手动刷新和统计接口。
 */
@RestController
@RequestMapping("/system/meta/cache")
public class MetaCacheController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MetaCacheController.class);

    @Autowired
    private MetaCacheService metaCacheService;

    /**
     * 刷新所有缓存（全量）
     * POST /system/meta/cache/refresh
     */
    @PostMapping("/refresh")
    
    public AjaxResult refreshAll() {
        log.info("管理员触发全量缓存刷新");
        metaCacheService.refreshAll();
        return AjaxResult.success("全量缓存刷新完成");
    }

    /**
     * 刷新单表缓存
     * POST /system/meta/cache/refresh/{tableCode}
     */
    @PostMapping("/refresh/{tableCode}")
    
    public AjaxResult refreshOne(@PathVariable String tableCode) {
        log.info("管理员触发单表缓存刷新 tableCode={}", tableCode);
        metaCacheService.refresh(tableCode);
        return AjaxResult.success("单表缓存刷新完成: " + tableCode);
    }

    /**
     * 获取缓存统计信息
     * GET /system/meta/cache/stats
     */
    @GetMapping("/stats")
    
    public AjaxResult getStats() {
        CacheStatsDTO stats = metaCacheService.getStats();
        return AjaxResult.success(stats);
    }

    /**
     * 清空所有缓存
     * POST /system/meta/cache/clear
     */
    @PostMapping("/clear")
    
    public AjaxResult clearAll() {
        log.warn("管理员触发清空所有缓存");
        metaCacheService.invalidateAll();
        return AjaxResult.success("所有缓存已清空");
    }

    /**
     * 使单表缓存失效
     * DELETE /system/meta/cache/{tableCode}
     */
    @DeleteMapping("/{tableCode}")
    
    public AjaxResult invalidate(@PathVariable String tableCode) {
        log.info("管理员触发缓存失效 tableCode={}", tableCode);
        metaCacheService.invalidate(tableCode);
        return AjaxResult.success("缓存已失效: " + tableCode);
    }
}
