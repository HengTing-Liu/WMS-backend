package com.abclonal.product.biz.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.PageUtil;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.service.I18nService;
import com.abclonal.product.service.system.service.SysEnumDefineService;
import com.abclonal.product.service.system.service.SysEnumItemService;
import com.abclonal.product.domain.converter.SysEnumDefineConverter;
import com.abclonal.product.domain.converter.SysEnumDefineComplexConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.abclonal.product.common.utils.PageUtils.startPage;

import com.abclonal.product.api.domain.request.sys.SysEnumDefineRequest;
import com.abclonal.product.api.domain.request.sys.SysEnumDefineBatchRequest;
import com.abclonal.product.api.domain.request.sys.SysEnumDefineWithItemsRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineResponse;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineWithItemsResponse;
import com.abclonal.product.dao.entity.SysEnumDefine;
import com.abclonal.product.dao.entity.SysEnumItem;

/**
 * 枚举定义表(SysEnumDefine)表业务层
 *
 * @author lht
 * @since 2026-03-06
 */
@Component
@Slf4j
public class SysEnumDefineBiz {

    @Autowired
    private SysEnumDefineService sysEnumDefineService;

    @Autowired
    private SysEnumItemService sysEnumItemService;

    @Autowired
    private I18nService i18nService;

    /**
     * 分页查询
     */
    public TableDataInfo<SysEnumDefineResponse> list(SysEnumDefineRequest sysEnumDefineRequest) {
        startPage();
        SysEnumDefine dto = SysEnumDefineConverter.INSTANCE.requestToSysEnumDefine(sysEnumDefineRequest);
        List<SysEnumDefine> list = sysEnumDefineService.queryByCondition(dto);
        return PageUtil.convertPage(list, this::toResponse);
    }

    private SysEnumDefineResponse toResponse(SysEnumDefine sysEnumDefine) {
        return SysEnumDefineConverter.INSTANCE.SysEnumDefineToResponse(sysEnumDefine);
    }

    /**
     * 通过主键查询单条数据
     */
    public R<SysEnumDefineResponse> queryById(@PathVariable("id") Long id) {
        SysEnumDefine sysEnumDefine = sysEnumDefineService.queryById(id);
        SysEnumDefineResponse response = SysEnumDefineConverter.INSTANCE.SysEnumDefineToResponse(sysEnumDefine);
        return R.ok(response);
    }

    /**
     * 新增数据
     */
    public R<SysEnumDefineResponse> add(SysEnumDefine sysEnumDefine) {
        if (sysEnumDefine.getCreateBy() == null) {
            sysEnumDefine.setCreateBy("lht");
            sysEnumDefine.setUpdateBy("lht");
        }

        Date now = new Date();
        sysEnumDefine.setCreateTime(now);
        sysEnumDefine.setUpdateTime(now);
        sysEnumDefine = sysEnumDefineService.insert(sysEnumDefine);
        SysEnumDefineResponse response = SysEnumDefineConverter.INSTANCE.SysEnumDefineToResponse(sysEnumDefine);
        return R.ok(response);
    }

    /**
     * 新增枚举定义及明细
     */
    @Transactional(rollbackFor = Exception.class)
    public R<SysEnumDefineWithItemsResponse> addWithItems(SysEnumDefineWithItemsRequest request) {
        // 1. 参数校验
        if (request.getEnumDefine() == null) {
            return R.fail(i18nService.getMessage("validation.enum.define.required"));
        }

        if (request.getEnumDefine().getEnumCode() == null || request.getEnumDefine().getEnumCode().isEmpty()) {
            return R.fail(i18nService.getMessage("validation.enum.code.required"));
        }
        if (request.getEnumDefine().getEnumName() == null || request.getEnumDefine().getEnumName().isEmpty()) {
            return R.fail(i18nService.getMessage("validation.enum.name.required"));
        }

        // 2. 获取当前用户
        String currentUser = "lht";
        try {
            currentUser = SecurityUtils.getUsername();
        } catch (Exception e) {
            log.warn("获取当前用户失败，使用默认用户: {}", currentUser);
        }

        // 3. 使用 Converter 转换枚举定义
        SysEnumDefine enumDefine = SysEnumDefineComplexConverter.INSTANCE.toEntity(request.getEnumDefine());
        Date now = new Date();
        enumDefine.setCreateBy(currentUser);
        enumDefine.setUpdateBy(currentUser);
        enumDefine.setCreateTime(now);
        enumDefine.setUpdateTime(now);

        // 4. 使用 Converter 转换枚举明细
        List<SysEnumItem> enumItems = null;
        if (request.getEnumItems() != null && !request.getEnumItems().isEmpty()) {
            enumItems = SysEnumDefineComplexConverter.INSTANCE.toItemEntityList(request.getEnumItems());
            for (SysEnumItem item : enumItems) {
                item.setCreateBy(currentUser);
                item.setUpdateBy(currentUser);
                item.setCreateTime(now);
                item.setUpdateTime(now);
            }
        }

        // 5. 执行新增
        Long enumDefineId = sysEnumDefineService.addEnumDefineWithItems(enumDefine, enumItems);

        // 6. 构建返回结果
        return R.ok(buildResponse(enumDefine, enumItems));
    }

    /**
     * 构建响应结果
     */
    private SysEnumDefineWithItemsResponse buildResponse(SysEnumDefine enumDefine, List<SysEnumItem> enumItems) {
        // 查询并返回明细列表
        List<SysEnumItem> savedItems = null;
        if (enumItems != null && !enumItems.isEmpty()) {
            SysEnumItem query = new SysEnumItem();
            query.setEnumCode(enumDefine.getEnumCode());
            savedItems = sysEnumItemService.queryByCondition(query);
        }

        // 使用 Converter 转换为响应对象
        return SysEnumDefineComplexConverter.INSTANCE.toResponse(enumDefine, savedItems);
    }

    /**
     * 批量新增数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> addBatch(SysEnumDefineBatchRequest sysEnumDefineBatchRequest) {
        List<SysEnumDefineRequest> sysEnumDefineList = sysEnumDefineBatchRequest.getRecords();

        if (sysEnumDefineList == null || sysEnumDefineList.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.add.list.empty"));
        }

        if (sysEnumDefineList.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.add.limit.exceed"));
        }

        try {
            Date now = new Date();
            String currentUser = SecurityUtils.getUsername();
            List<SysEnumDefine> sysEnumDefineEntityList = new ArrayList<>();

            for (SysEnumDefineRequest request : sysEnumDefineList) {
                SysEnumDefine dto = SysEnumDefineConverter.INSTANCE.requestToSysEnumDefine(request);
                dto.setCreateBy(currentUser);
                dto.setUpdateBy(currentUser);
                dto.setCreateTime(now);
                dto.setUpdateTime(now);
                sysEnumDefineEntityList.add(dto);
            }

            int success = sysEnumDefineService.insertBatchAtomic(sysEnumDefineEntityList);
            return R.ok(true, i18nService.getMessage("batch.add.success", sysEnumDefineList.size()));

        } catch (Exception e) {
            return R.fail(i18nService.getMessage("batch.add.failed", e.getMessage()));
        }
    }

    /**
     * 批量删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.delete.list.empty"));
        }

        if (ids.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.delete.limit.exceed"));
        }

        try {
            String currentUser = SecurityUtils.getLoginUser().getUsername();
            int success = sysEnumDefineService.logicDeleteBatchByIds(ids, currentUser);

            if (success == ids.size()) {
                return R.ok(true, i18nService.getMessage("batch.delete.success", ids.size()));
            } else if (success > 0) {
                return R.ok(false, i18nService.getMessage("batch.delete.partial.success", success, ids.size() - success));
            } else {
                return R.fail(i18nService.getMessage("batch.delete.failed"));
            }
        } catch (Exception e) {
            return R.fail(i18nService.getMessage("batch.delete.error", e.getMessage()));
        }
    }

    /**
     * 批量修改数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateBatch(SysEnumDefineBatchRequest sysEnumDefineBatchRequest) {
        List<SysEnumDefineRequest> sysEnumDefineList = sysEnumDefineBatchRequest.getRecords();

        if (sysEnumDefineList == null || sysEnumDefineList.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.update.list.empty"));
        }

        if (sysEnumDefineList.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.update.limit.exceed"));
        }

        Date now = new Date();
        List<SysEnumDefine> sysEnumDefineEntityList = new ArrayList<>();

        for (int i = 0; i < sysEnumDefineList.size(); i++) {
            SysEnumDefineRequest request = sysEnumDefineList.get(i);

            if (request.getId() == null) {
                return R.fail(i18nService.getMessage("batch.update.id.required", i + 1));
            }

            SysEnumDefine existing = sysEnumDefineService.queryById(request.getId());
            if (existing == null) {
                return R.fail(i18nService.getMessage("batch.update.data.not.found", request.getId(), i + 1));
            }

            SysEnumDefine dto = SysEnumDefineConverter.INSTANCE.requestToSysEnumDefine(request);
            dto.setUpdateBy(SecurityUtils.getUsername());
            dto.setUpdateTime(now);
            sysEnumDefineEntityList.add(dto);
        }

        try {
            int success = sysEnumDefineService.updateBatchAtomic(sysEnumDefineEntityList);
            return R.ok(true, i18nService.getMessage("batch.update.success", sysEnumDefineList.size()));
        } catch (Exception e) {
            return R.fail(i18nService.getMessage("batch.update.failed", e.getMessage()));
        }
    }

}
