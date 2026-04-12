package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.MetaPublishExecuteRequest;
import com.abtk.product.api.domain.request.sys.MetaPublishPlanRequest;
import com.abtk.product.api.domain.request.sys.MetaPublishQueryRequest;
import com.abtk.product.api.domain.response.sys.MetaPublishPlanResponse;
import com.abtk.product.api.domain.response.sys.MetaPublishResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.dao.entity.MetaPublish;
import com.abtk.product.service.sys.service.MetaPublishService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 低代码元数据发布控制器
 */
@RestController
@RequestMapping("/api/system/meta/publish")
public class MetaPublishController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MetaPublishController.class);

    @Autowired
    private MetaPublishService metaPublishService;

    /**
     * 生成发布计划（预览 Diff + SQL）
     */
    @RequiresPermissions("system:meta:publish:plan")
    @PostMapping("/plan")
    public R<List<MetaPublishPlanResponse>> generatePlan(
            @Validated @RequestBody MetaPublishPlanRequest request) {
        return R.ok(metaPublishService.generatePlans(request));
    }

    /**
     * 保存发布计划（生成发布编码，等待执行）
     */
    @RequiresPermissions("system:meta:publish:execute")
    @PostMapping("/save")
    public R<MetaPublish> savePlan(
            @Validated @RequestBody MetaPublishPlanRequest request) {
        MetaPublish publish = metaPublishService.savePlan(request);
        return R.ok(publish);
    }

    /**
     * 执行发布
     */
    @RequiresPermissions("system:meta:publish:execute")
    @PostMapping("/execute")
    public R<MetaPublishResponse> execute(
            @Validated @RequestBody MetaPublishExecuteRequest request) {
        MetaPublishResponse result = metaPublishService.execute(request);
        return R.ok(result);
    }

    /**
     * 查询发布历史
     */
    @RequiresPermissions("system:meta:publish:query")
    @GetMapping("/history")
    public R<List<MetaPublishResponse>> getHistory(MetaPublishQueryRequest request) {
        return R.ok(metaPublishService.queryHistory(request));
    }

    /**
     * 查询发布详情
     */
    @RequiresPermissions("system:meta:publish:query")
    @GetMapping("/{id}")
    public R<MetaPublishResponse> getById(@PathVariable Long id) {
        return R.ok(metaPublishService.getById(id));
    }

    /**
     * 通过发布编码查询
     */
    @RequiresPermissions("system:meta:publish:query")
    @GetMapping("/code/{publishCode}")
    public R<MetaPublishResponse> getByCode(@PathVariable String publishCode) {
        return R.ok(metaPublishService.getByCode(publishCode));
    }

    /**
     * 回滚（V2）
     */
    @RequiresPermissions("system:meta:publish:execute")
    @PostMapping("/rollback/{id}")
    public R<Void> rollback(@PathVariable Long id) {
        metaPublishService.rollback(id);
        return R.ok();
    }
}
