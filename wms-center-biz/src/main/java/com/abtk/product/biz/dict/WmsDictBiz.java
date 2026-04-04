package com.abtk.product.biz.dict;

import com.abtk.product.api.domain.request.dict.WmsDictRequest;
import com.abtk.product.api.domain.response.dict.WmsDictResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.WmsDict;
import com.abtk.product.service.dict.service.WmsDictService;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 字典管理业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-26
 */
@Slf4j
@Component
public class WmsDictBiz {

    @Autowired
    private WmsDictService wmsDictService;

    @Autowired
    private I18nService i18nService;

    // ==================== 基础CRUD方法 ====================

    /**
     * 分页查询
     */
    public R<List<WmsDictResponse>> list(WmsDictRequest request) {
        WmsDict condition = new WmsDict();
        BeanUtils.copyProperties(request, condition);
        List<WmsDict> list = wmsDictService.queryByCondition(condition);
        List<WmsDictResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 查询所有（不分页）
     */
    public R<List<WmsDictResponse>> listAll() {
        List<WmsDict> list = wmsDictService.listAll();
        List<WmsDictResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 通过ID查询
     */
    public R<WmsDictResponse> queryById(Long id) {
        WmsDict entity = wmsDictService.queryById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.dict.not.found"));
        }
        return R.ok(convertToResponse(entity));
    }

    /**
     * 新增数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> add(WmsDictRequest request) {
        // 校验字典编码唯一性
        if (!wmsDictService.checkDictCodeUnique(request.getDictCode())) {
            return R.fail("字典编码已存在");
        }

        WmsDict entity = convertToEntity(request);
        fillSystemFields(entity);

        WmsDict result = wmsDictService.insert(entity);
        return R.ok(result.getId());
    }

    /**
     * 编辑数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> update(Long id, WmsDictRequest request) {
        WmsDict existing = wmsDictService.queryById(id);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.dict.not.found"));
        }

        // 如果修改了字典编码，校验唯一性
        if (request.getDictCode() != null
                && !request.getDictCode().equals(existing.getDictCode())) {
            if (!wmsDictService.checkDictCodeUnique(request.getDictCode())) {
                return R.fail("字典编码已存在");
            }
        }

        WmsDict entity = convertToEntity(request);
        entity.setId(id);
        entity.setUpdateTime(new Date());

        wmsDictService.update(entity);
        return R.ok();
    }

    /**
     * 删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> delete(Long id) {
        WmsDict existing = wmsDictService.queryById(id);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.dict.not.found"));
        }

        wmsDictService.logicDeleteById(id, "system");
        return R.ok();
    }

    /**
     * 切换状态
     */
    public R<Void> toggleStatus(Long id, Integer enabled) {
        WmsDict existing = wmsDictService.queryById(id);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.dict.not.found"));
        }

        wmsDictService.toggleStatus(id, enabled);
        return R.ok();
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 填充系统字段
     */
    private void fillSystemFields(WmsDict entity) {
        Date now = new Date();
        entity.setCreateBy("system");
        entity.setUpdateBy("system");
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setIsDeleted(0);
    }

    /**
     * Entity 转 Response
     */
    private WmsDictResponse convertToResponse(WmsDict entity) {
        if (entity == null) {
            return null;
        }
        WmsDictResponse response = new WmsDictResponse();
        BeanUtils.copyProperties(entity, response);
        // 兼容前端字段名
        response.setId(entity.getId());
        return response;
    }

    /**
     * Entity列表转Response列表
     */
    private List<WmsDictResponse> convertToResponseList(List<WmsDict> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<WmsDictResponse> result = new ArrayList<>();
        for (WmsDict entity : list) {
            result.add(convertToResponse(entity));
        }
        return result;
    }

    /**
     * Request 转 Entity
     */
    private WmsDict convertToEntity(WmsDictRequest request) {
        if (request == null) {
            return null;
        }
        WmsDict entity = new WmsDict();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }
}
