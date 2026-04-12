package com.abtk.product.service.sys.service;

import com.abtk.product.api.domain.request.sys.MetaPublishExecuteRequest;
import com.abtk.product.api.domain.request.sys.MetaPublishPlanRequest;
import com.abtk.product.api.domain.request.sys.MetaPublishQueryRequest;
import com.abtk.product.api.domain.response.sys.MetaPublishPlanResponse;
import com.abtk.product.api.domain.response.sys.MetaPublishResponse;
import com.abtk.product.dao.entity.MetaPublish;

import java.util.List;

/**
 * 元数据发布服务接口
 */
public interface MetaPublishService {

    /**
     * 生成发布计划（校验 + Diff + SQL预览）
     */
    MetaPublishPlanResponse generatePlan(MetaPublishPlanRequest request);

    /**
     * 批量生成发布计划
     */
    List<MetaPublishPlanResponse> generatePlans(MetaPublishPlanRequest request);

    /**
     * 保存发布计划到数据库（plan 预览确认后正式创建）
     */
    MetaPublish savePlan(MetaPublishPlanRequest request);

    /**
     * 执行发布
     */
    MetaPublishResponse execute(MetaPublishExecuteRequest request);

    /**
     * 查询发布历史
     */
    List<MetaPublishResponse> queryHistory(MetaPublishQueryRequest request);

    /**
     * 查询发布详情
     */
    MetaPublishResponse getById(Long id);

    /**
     * 查询发布详情（通过发布编码）
     */
    MetaPublishResponse getByCode(String publishCode);

    /**
     * 快照回滚
     */
    void rollback(Long publishId);
}
