package com.abtk.product.biz.user;

import com.abtk.product.api.domain.request.user.WmsUserRequest;
import com.abtk.product.api.domain.response.user.WmsUserResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.WmsUser;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.service.user.service.WmsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户管理业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-26
 */
@Slf4j
@Component
public class WmsUserBiz {

    @Autowired
    private WmsUserService wmsUserService;

    @Autowired
    private I18nService i18nService;

    // ==================== 基础CRUD方法 ====================

    /**
     * 分页查询
     */
    public R<List<WmsUserResponse>> list(WmsUserRequest request) {
        WmsUser condition = new WmsUser();
        BeanUtils.copyProperties(request, condition);
        List<WmsUser> list = wmsUserService.queryByCondition(condition);
        List<WmsUserResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 查询所有（不分页）
     */
    public R<List<WmsUserResponse>> listAll() {
        List<WmsUser> list = wmsUserService.listAll();
        List<WmsUserResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 通过ID查询
     */
    public R<WmsUserResponse> queryById(Long userId) {
        WmsUser entity = wmsUserService.queryById(userId);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.user.not.found"));
        }
        return R.ok(convertToResponse(entity));
    }

    /**
     * 新增数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> add(WmsUserRequest request) {
        // 校验用户名唯一性
        if (!wmsUserService.checkUserNameUnique(request.getUserName())) {
            return R.fail("用户名已存在");
        }

        WmsUser entity = convertToEntity(request);
        fillSystemFields(entity);

        WmsUser result = wmsUserService.insert(entity);
        return R.ok(result.getUserId());
    }

    /**
     * 编辑数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> update(Long userId, WmsUserRequest request) {
        WmsUser existing = wmsUserService.queryById(userId);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.user.not.found"));
        }

        // 如果修改了用户名，校验唯一性
        if (request.getUserName() != null
                && !request.getUserName().equals(existing.getUserName())) {
            if (!wmsUserService.checkUserNameUnique(request.getUserName())) {
                return R.fail("用户名已存在");
            }
        }

        WmsUser entity = convertToEntity(request);
        entity.setUserId(userId);
        entity.setUpdateTime(new Date());

        wmsUserService.update(entity);
        return R.ok();
    }

    /**
     * 删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> delete(Long userId) {
        WmsUser existing = wmsUserService.queryById(userId);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.user.not.found"));
        }

        wmsUserService.logicDeleteById(userId, "system");
        return R.ok();
    }

    /**
     * 切换状态
     */
    public R<Void> toggleStatus(Long userId, Integer enabled) {
        WmsUser existing = wmsUserService.queryById(userId);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.user.not.found"));
        }

        String status = (enabled != null && enabled == 1) ? "0" : "1";
        wmsUserService.toggleStatus(userId, status);
        return R.ok();
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 填充系统字段
     */
    private void fillSystemFields(WmsUser entity) {
        Date now = new Date();
        entity.setCreateBy("system");
        entity.setUpdateBy("system");
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setDelFlag("0");
    }

    /**
     * Entity 转 Response（密码不返回）
     */
    private WmsUserResponse convertToResponse(WmsUser entity) {
        if (entity == null) {
            return null;
        }
        WmsUserResponse response = new WmsUserResponse();
        BeanUtils.copyProperties(entity, response);
        // WmsUserResponse 已不含 password 字段，安全
        return response;
    }

    /**
     * Entity列表转Response列表
     */
    private List<WmsUserResponse> convertToResponseList(List<WmsUser> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<WmsUserResponse> result = new ArrayList<>();
        for (WmsUser entity : list) {
            result.add(convertToResponse(entity));
        }
        return result;
    }

    /**
     * Request 转 Entity
     */
    private WmsUser convertToEntity(WmsUserRequest request) {
        if (request == null) {
            return null;
        }
        WmsUser entity = new WmsUser();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }
}
