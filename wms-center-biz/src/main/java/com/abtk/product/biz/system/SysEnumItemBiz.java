package com.abtk.product.biz.system;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.PageUtil;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.utils.bean.BeanValidators;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.domain.LoginUser;
import com.abtk.product.service.security.TokenService;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.*;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import static com.abtk.product.common.utils.PageUtils.startPage;
import com.abtk.product.domain.converter.SysEnumItemConverter;
import com.abtk.product.api.domain.request.sys.SysEnumItemRequest;
import com.abtk.product.api.domain.response.sys.SysEnumItemResponse;
import com.abtk.product.dao.entity.SysEnumItem;
import com.abtk.product.dao.mapper.SysEnumItemMapper;
import com.abtk.product.service.system.service.SysEnumItemService;
import com.abtk.product.api.domain.request.sys.SysEnumItemBatchRequest;

@Component
public class SysEnumItemBiz {
    @Autowired
    private SysEnumItemService sysEnumItemService;

    @Autowired
    private I18nService i18nService;

    public TableDataInfo<SysEnumItemResponse> list(SysEnumItemRequest sysEnumItemRequest) {
        startPage();
        SysEnumItem dto = SysEnumItemConverter.INSTANCE.requestToSysEnumItem(sysEnumItemRequest);
        List<SysEnumItem> list = sysEnumItemService.queryByCondition(dto);
        TableDataInfo<SysEnumItemResponse> tableDataInfo = PageUtil.convertPage(list, this::toResponse);
        return tableDataInfo;
    }

    private SysEnumItemResponse toResponse(SysEnumItem sysEnumItem) {
        SysEnumItemResponse sysEnumItemResponse = SysEnumItemConverter.INSTANCE.SysEnumItemToResponse(sysEnumItem);
        return sysEnumItemResponse;
    }
    
       public R<SysEnumItemResponse> queryById(@PathVariable("id") Long id) {
        SysEnumItem  sysEnumItem = sysEnumItemService.queryById(id);
        SysEnumItemResponse sysEnumItemResponse = SysEnumItemConverter.INSTANCE.SysEnumItemToResponse(sysEnumItem);
        return R.ok(sysEnumItemResponse);
    }
        
        public R<SysEnumItemResponse> add(SysEnumItem sysEnumItem) {
         if(sysEnumItem.getCreateBy()==null){
              sysEnumItem.setCreateBy("lht");
              sysEnumItem.setUpdateBy("lht");
           }
          
         java.util.Date now = new java.util.Date();
         sysEnumItem.setCreateTime(now);
         sysEnumItem.setUpdateTime(now);
         sysEnumItem = sysEnumItemService.insert(sysEnumItem);
         SysEnumItemResponse sysEnumItemResponse = SysEnumItemConverter.INSTANCE.SysEnumItemToResponse(sysEnumItem);
         return R.ok(sysEnumItemResponse);
    }
    
    @Transactional(rollbackFor = Exception.class)  // 开启事务，任何异常都回滚
    public R<Boolean> addBatch(SysEnumItemBatchRequest sysEnumItemBatchRequest) {
        List<SysEnumItemRequest> sysEnumItemList = sysEnumItemBatchRequest.getRecords();
        // 1. 参数校验
        if (sysEnumItemList == null || sysEnumItemList.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.add.list.empty"));
        }

        // 2. 批量大小限制
        if (sysEnumItemList.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.add.limit.exceed"));
        }

        try {
            // 3. 前置校验：检查所有数据的合法性
            java.util.Date now = new java.util.Date();
            String currentUser = SecurityUtils.getUsername();
            List<SysEnumItem> sysEnumItemEntityList = new ArrayList<>();

            for (int i = 0; i < sysEnumItemList.size(); i++) {
                SysEnumItemRequest request = sysEnumItemList.get(i);

                // 3.1 业务校验（根据你的业务需求）
  
                // 3.2 可以添加其他校验，比如唯一性校验

                // 3.3 转换DTO
                SysEnumItem dto = SysEnumItemConverter.INSTANCE.requestToSysEnumItem(request);
                dto.setCreateBy(currentUser);
                dto.setUpdateBy(currentUser);
                dto.setCreateTime(now);
                dto.setUpdateTime(now);
                sysEnumItemEntityList.add(dto);
            }
            // 4. 批量插入（在事务中执行）
            int success = sysEnumItemService.insertBatchAtomic(sysEnumItemEntityList);

            // 5. 返回结果
            return R.ok(true, i18nService.getMessage("batch.add.success", sysEnumItemList.size()));

        } catch (Exception e) {
            // 事务会自动回滚
            return R.fail(i18nService.getMessage("batch.add.failed", e.getMessage()));
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteBatch(List<Long> ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.delete.list.empty"));
        }

        // 可以添加批量大小限制，防止一次删除太多数据
        if (ids.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.delete.limit.exceed"));
        }

        try {
            // 设置删除人信息（如果有逻辑删除）
            String currentUser =  SecurityUtils.getLoginUser().getUsername(); // 可以从上下文中获取当前登录用户

            // 批量删除
            int success = sysEnumItemService.logicDeleteBatchByIds(ids,currentUser);

            // 判断是否全部删除成功
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
    
    
    @Transactional(rollbackFor = Exception.class)  // 开启事务，任何异常都回滚
    public R<Boolean> updateBatch(@RequestBody SysEnumItemBatchRequest sysEnumItemBatchRequest) {
        List<SysEnumItemRequest> sysEnumItemList = sysEnumItemBatchRequest.getRecords();

        // 1. 参数校验
        if (sysEnumItemList == null || sysEnumItemList.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.update.list.empty"));
        }

        // 2. 批量大小限制
        if (sysEnumItemList.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.update.limit.exceed"));
        }

        // 3. 前置校验：检查所有数据的合法性
        java.util.Date now = new java.util.Date();
        List<SysEnumItem> sysEnumItemEntityList = new ArrayList<>();

        for (int i = 0; i < sysEnumItemList.size(); i++) {
            SysEnumItemRequest request = sysEnumItemList.get(i);

            // 3.1 检查每条数据是否有ID
            if (request.getId() == null) {
                return R.fail(i18nService.getMessage("batch.update.id.required", i + 1));
            }

            // 3.2 检查数据是否存在（可选，如果需要的话）
            SysEnumItem existing = sysEnumItemService.queryById(request.getId());
            if (existing == null) {
                return R.fail(i18nService.getMessage("batch.update.data.not.found", request.getId(), i + 1));
            }

            // 3.3 其他业务校验


            // 3.4 转换DTO
            SysEnumItem dto = SysEnumItemConverter.INSTANCE.requestToSysEnumItem(request);
            dto.setUpdateBy(SecurityUtils.getUsername());
            dto.setUpdateTime(now);

            sysEnumItemEntityList.add(dto);
        }

        try {
            // 4. 执行批量更新（在事务中执行）
            int success = sysEnumItemService.updateBatchAtomic(sysEnumItemEntityList);

            // 5. 返回结果
            return R.ok(true, i18nService.getMessage("batch.update.success", sysEnumItemList.size()));

        } catch (Exception e) {
            // 事务会自动回滚
            return R.fail(i18nService.getMessage("batch.update.failed", e.getMessage()));
        }
    }
   
}
