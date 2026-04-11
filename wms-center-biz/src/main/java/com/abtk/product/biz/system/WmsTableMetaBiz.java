package com.abtk.product.biz.system;

import com.abtk.product.api.domain.request.system.WmsTableMetaRequest;
import com.abtk.product.api.domain.response.system.WmsTableMetaResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.WmsTableMeta;
import com.abtk.product.service.system.service.IWmsTableMetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 表元数据管理业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-04-06
 */
@Slf4j
@Component
public class WmsTableMetaBiz {

    @Autowired
    private IWmsTableMetaService wmsTableMetaService;

    // ==================== 基础CRUD方法 ====================

    /**
     * 分页查询
     */
    public R<List<WmsTableMetaResponse>> list(WmsTableMetaRequest request) {
        WmsTableMeta condition = new WmsTableMeta();
        BeanUtils.copyProperties(request, condition);
        List<WmsTableMeta> list = wmsTableMetaService.queryByCondition(condition);
        List<WmsTableMetaResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 查询所有（不分页）
     */
    public R<List<WmsTableMetaResponse>> listAll() {
        List<WmsTableMeta> list = wmsTableMetaService.listAll();
        List<WmsTableMetaResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 通过ID查询
     */
    public R<WmsTableMetaResponse> queryById(Long id) {
        WmsTableMeta entity = wmsTableMetaService.queryById(id);
        if (entity == null) {
            return R.fail("表元数据不存在");
        }
        return R.ok(convertToResponse(entity));
    }

    /**
     * 通过编码查询
     */
    public R<WmsTableMetaResponse> queryByCode(String tableCode) {
        WmsTableMeta entity = wmsTableMetaService.queryByCode(tableCode);
        if (entity == null) {
            return R.fail("表元数据不存在");
        }
        return R.ok(convertToResponse(entity));
    }

    /**
     * 新增数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> add(WmsTableMetaRequest request) {
        // 校验表编码唯一性
        if (!wmsTableMetaService.checkTableCodeUnique(request.getTableCode())) {
            return R.fail("表编码已存在");
        }

        WmsTableMeta entity = convertToEntity(request);
        fillSystemFields(entity);

        WmsTableMeta result = wmsTableMetaService.insert(entity);
        return R.ok(result.getId());
    }

    /**
     * 编辑数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> update(Long id, WmsTableMetaRequest request) {
        WmsTableMeta existing = wmsTableMetaService.queryById(id);
        if (existing == null) {
            return R.fail("表元数据不存在");
        }

        // 如果修改了表编码，校验唯一性
        if (request.getTableCode() != null
                && !request.getTableCode().equals(existing.getTableCode())) {
            if (!wmsTableMetaService.checkTableCodeUnique(request.getTableCode())) {
                return R.fail("表编码已存在");
            }
        }

        WmsTableMeta entity = convertToEntity(request);
        entity.setId(id);
        entity.setUpdateTime(new Date());

        wmsTableMetaService.update(entity);
        return R.ok();
    }

    /**
     * 删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> delete(Long id) {
        WmsTableMeta existing = wmsTableMetaService.queryById(id);
        if (existing == null) {
            return R.fail("表元数据不存在");
        }

        // 检查是否有关联字段
        if (wmsTableMetaService.hasRelatedFields(id)) {
            return R.fail("该表存在关联字段，无法删除");
        }

        wmsTableMetaService.logicDeleteById(id, "system");
        return R.ok();
    }

    /**
     * 切换状态
     */
    public R<Void> toggleStatus(Long id) {
        WmsTableMeta existing = wmsTableMetaService.queryById(id);
        if (existing == null) {
            return R.fail("表元数据不存在");
        }

        // 切换状态：1->0, 0->1
        Integer newStatus = existing.getStatus() != null && existing.getStatus() == 1 ? 0 : 1;
        wmsTableMetaService.toggleStatus(id, newStatus);
        return R.ok();
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 填充系统字段
     */
    private void fillSystemFields(WmsTableMeta entity) {
        Date now = new Date();
        entity.setCreateBy("system");
        entity.setUpdateBy("system");
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
    }

    /**
     * Entity 转 Response
     */
    private WmsTableMetaResponse convertToResponse(WmsTableMeta entity) {
        if (entity == null) {
            return null;
        }
        WmsTableMetaResponse response = new WmsTableMetaResponse();
        BeanUtils.copyProperties(entity, response);
        // 兼容前端字段名
        response.setId(entity.getId());
        return response;
    }

    /**
     * Entity列表转Response列表
     */
    private List<WmsTableMetaResponse> convertToResponseList(List<WmsTableMeta> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<WmsTableMetaResponse> result = new ArrayList<>();
        for (WmsTableMeta entity : list) {
            result.add(convertToResponse(entity));
        }
        return result;
    }

    /**
     * Request 转 Entity
     */
    private WmsTableMeta convertToEntity(WmsTableMetaRequest request) {
        if (request == null) {
            return null;
        }
        WmsTableMeta entity = new WmsTableMeta();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }
}
