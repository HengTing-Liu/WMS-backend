package com.abclonal.product.biz.sys;

import com.abclonal.product.api.domain.request.location.WmsLocationRequest;
import com.abclonal.product.api.domain.response.location.WmsLocationResponse;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.bean.BeanUtils;
import com.abclonal.product.dao.entity.WmsLocation;
import com.abclonal.product.domain.converter.WmsLocationConverter;
import com.abclonal.product.service.location.service.WmsLocationService;
import com.abclonal.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 库位档案业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-26
 */
@Slf4j
@Component
public class LocationBiz {

    @Autowired
    private WmsLocationService wmsLocationService;

    @Autowired
    private I18nService i18nService;

    /**
     * 查询库位列表
     */
    public R<List<WmsLocation>> list(WmsLocationRequest queryRequest) {
        WmsLocation condition = new WmsLocation();
        BeanUtils.copyProperties(queryRequest, condition);
        List<WmsLocation> list = wmsLocationService.queryByCondition(condition);
        return R.ok(list);
    }

    /**
     * 查询所有库位（不分页）
     */
    public R<List<WmsLocation>> listAll() {
        List<WmsLocation> list = wmsLocationService.listAll();
        return R.ok(list);
    }

    /**
     * 根据ID查询库位详情
     */
    public R<WmsLocationResponse> queryById(Long id) {
        WmsLocation entity = wmsLocationService.queryById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.location.not.found"));
        }
        return R.ok(WmsLocationConverter.INSTANCE.entityToResponse(entity));
    }

    /**
     * 新增库位
     */
    public R<Long> add(WmsLocationRequest request) {
        WmsLocation location = WmsLocationConverter.INSTANCE.requestToEntity(request);
        location.setIsDeleted(0);
        wmsLocationService.insert(location);
        return R.ok(location.getId());
    }

    /**
     * 更新库位
     */
    public R<Void> update(Long id, WmsLocationRequest request) {
        WmsLocation location = WmsLocationConverter.INSTANCE.requestToEntity(request);
        location.setId(id);
        wmsLocationService.update(location);
        return R.ok();
    }

    /**
     * 删除库位（逻辑删除）
     */
    public R<Void> delete(Long id) {
        wmsLocationService.logicDeleteById(id, "system");
        return R.ok();
    }

    /**
     * 切换库位状态
     */
    public R<Void> toggleStatus(Long id, Integer enabled) {
        wmsLocationService.toggleStatus(id, enabled);
        return R.ok();
    }
}
