package com.abclonal.product.biz.sys;

import com.abclonal.product.api.domain.request.sys.StorageQueryRequest;
import com.abclonal.product.api.domain.request.sys.StorageRequest;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.bean.BeanUtils;
import com.abclonal.product.dao.entity.Storage;
import com.abclonal.product.domain.converter.StorageConverter;
import com.abclonal.product.service.sys.service.StorageService;
import com.abclonal.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 库区档案业务层
 * WMS0050 库区管理
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-26
 */
@Slf4j
@Component
public class StorageBiz {

    @Autowired
    private StorageService storageService;

    @Autowired
    private I18nService i18nService;

    /**
     * 查询库区列表
     */
    public R<List<Storage>> list(StorageQueryRequest queryRequest) {
        Storage condition = new Storage();
        BeanUtils.copyProperties(queryRequest, condition);
        List<Storage> list = storageService.list(condition);
        return R.ok(list);
    }

    /**
     * 查询所有库区
     */
    public R<List<Storage>> listAll() {
        List<Storage> list = storageService.listAll();
        return R.ok(list);
    }

    /**
     * 根据ID查询库区详情
     */
    public R<Storage> queryById(Long id) {
        Storage entity = storageService.getById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.storage.not.found"));
        }
        return R.ok(entity);
    }

    /**
     * 新增库区
     */
    public R<Long> add(StorageRequest request) {
        // 使用Converter转换Request到Entity
        Storage storage = StorageConverter.INSTANCE.requestToEntity(request);

        Long id = storageService.create(storage);
        return R.ok(id);
    }

    /**
     * 更新库区
     */
    public R<Void> update(Long id, StorageRequest request) {
        // 使用Converter转换Request到Entity
        Storage storage = StorageConverter.INSTANCE.requestToEntity(request);

        storageService.update(id, storage);
        return R.ok();
    }

    /**
     * 删除库区
     */
    public R<Void> delete(Long id) {
        storageService.delete(id);
        return R.ok();
    }

    /**
     * 切换库区状态
     */
    public R<Void> toggleStatus(Long id, Integer enabled) {
        storageService.toggleStatus(id, enabled);
        return R.ok();
    }
}
