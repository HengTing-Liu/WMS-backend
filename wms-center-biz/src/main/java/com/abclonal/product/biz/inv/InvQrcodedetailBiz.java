package com.abclonal.product.biz.inv;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.PageUtil;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import static com.abclonal.product.common.utils.PageUtils.startPage;
import com.abclonal.product.domain.converter.InvQrcodedetailConverter;
import com.abclonal.product.api.domain.request.inv.InvQrcodedetailRequest;
import com.abclonal.product.api.domain.response.inv.InvQrcodedetailResponse;
import com.abclonal.product.dao.entity.InvQrcodedetail;
import com.abclonal.product.service.inv.service.InvQrcodedetailService;
import com.abclonal.product.api.domain.request.inv.InvQrcodedetailBatchRequest;

@Component
public class InvQrcodedetailBiz {
    @Autowired
    private InvQrcodedetailService invQrcodedetailService;

    @Autowired
    private I18nService i18nService;

    public TableDataInfo<InvQrcodedetailResponse> list(InvQrcodedetailRequest invQrcodedetailRequest) {
        startPage();
        InvQrcodedetail dto = InvQrcodedetailConverter.INSTANCE.requestToInvQrcodedetail(invQrcodedetailRequest);
        List<InvQrcodedetail> list = invQrcodedetailService.queryByCondition(dto);
        TableDataInfo<InvQrcodedetailResponse> tableDataInfo = PageUtil.convertPage(list, this::toResponse);
        return tableDataInfo;
    }

    private InvQrcodedetailResponse toResponse(InvQrcodedetail invQrcodedetail) {
        InvQrcodedetailResponse invQrcodedetailResponse = InvQrcodedetailConverter.INSTANCE.InvQrcodedetailToResponse(invQrcodedetail);
        return invQrcodedetailResponse;
    }
    
       public R<InvQrcodedetailResponse> queryById(@PathVariable("id") Long id) {
        InvQrcodedetail  invQrcodedetail = invQrcodedetailService.queryById(id);
        InvQrcodedetailResponse invQrcodedetailResponse = InvQrcodedetailConverter.INSTANCE.InvQrcodedetailToResponse(invQrcodedetail);
        return R.ok(invQrcodedetailResponse);
    }
        
        public R<InvQrcodedetailResponse> add(InvQrcodedetail invQrcodedetail) {
         if(invQrcodedetail.getCreateBy()==null){
              invQrcodedetail.setCreateBy("lht");
              invQrcodedetail.setUpdateBy("lht");
           }
          
         java.util.Date now = new java.util.Date();
         invQrcodedetail.setCreateTime(now);
         invQrcodedetail.setUpdateTime(now);
         invQrcodedetail = invQrcodedetailService.insert(invQrcodedetail);
         InvQrcodedetailResponse invQrcodedetailResponse = InvQrcodedetailConverter.INSTANCE.InvQrcodedetailToResponse(invQrcodedetail);
         return R.ok(invQrcodedetailResponse);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> addBatch(InvQrcodedetailBatchRequest invQrcodedetailBatchRequest) {
        List<InvQrcodedetailRequest> invQrcodedetailList = invQrcodedetailBatchRequest.getRecords();
        // 1. 参数校验
        if (invQrcodedetailList == null || invQrcodedetailList.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.add.list.empty"));
        }

        // 2. 批量大小限制
        if (invQrcodedetailList.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.add.limit.exceed"));
        }

        try {
            // 3. 前置校验：检查所有数据的合法性
            java.util.Date now = new java.util.Date();
            String currentUser = SecurityUtils.getUsername();
            List<InvQrcodedetail> invQrcodedetailEntityList = new ArrayList<>();

            for (int i = 0; i < invQrcodedetailList.size(); i++) {
                InvQrcodedetailRequest request = invQrcodedetailList.get(i);

                // 3.1 业务校验（根据你的业务需求）

                // 3.2 可以添加其他校验，比如唯一性校验

                // 3.3 转换DTO
                InvQrcodedetail dto = InvQrcodedetailConverter.INSTANCE.requestToInvQrcodedetail(request);
                dto.setCreateBy(currentUser);
                dto.setUpdateBy(currentUser);
                dto.setCreateTime(now);
                dto.setUpdateTime(now);
                invQrcodedetailEntityList.add(dto);
            }
            // 4. 批量插入（在事务中执行）
            int success = invQrcodedetailService.insertBatchAtomic(invQrcodedetailEntityList);

            // 5. 返回结果
            return R.ok(true, i18nService.getMessage("batch.add.success", invQrcodedetailList.size()));

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
            String currentUser = SecurityUtils.getLoginUser().getUsername();

            // 批量删除
            int success = invQrcodedetailService.logicDeleteBatchByIds(ids, currentUser);

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
    
    
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateBatch(@RequestBody InvQrcodedetailBatchRequest invQrcodedetailBatchRequest) {
        List<InvQrcodedetailRequest> invQrcodedetailList = invQrcodedetailBatchRequest.getRecords();

        // 1. 参数校验
        if (invQrcodedetailList == null || invQrcodedetailList.isEmpty()) {
            return R.fail(i18nService.getMessage("batch.update.list.empty"));
        }

        // 2. 批量大小限制
        if (invQrcodedetailList.size() > 1000) {
            return R.fail(i18nService.getMessage("batch.update.limit.exceed"));
        }

        // 3. 前置校验：检查所有数据的合法性
        java.util.Date now = new java.util.Date();
        List<InvQrcodedetail> invQrcodedetailEntityList = new ArrayList<>();

        for (int i = 0; i < invQrcodedetailList.size(); i++) {
            InvQrcodedetailRequest request = invQrcodedetailList.get(i);

            // 3.1 检查每条数据是否有ID
            if (request.getId() == null) {
                return R.fail(i18nService.getMessage("batch.update.id.required", i + 1));
            }

            // 3.2 检查数据是否存在（可选，如果需要的话）
            InvQrcodedetail existing = invQrcodedetailService.queryById(request.getId());
            if (existing == null) {
                return R.fail(i18nService.getMessage("batch.update.data.not.found", request.getId(), i + 1));
            }

            // 3.3 其他业务校验

            // 3.4 转换DTO
            InvQrcodedetail dto = InvQrcodedetailConverter.INSTANCE.requestToInvQrcodedetail(request);
            dto.setUpdateBy(SecurityUtils.getUsername());
            dto.setUpdateTime(now);

            invQrcodedetailEntityList.add(dto);
        }

        try {
            // 4. 执行批量更新（在事务中执行）
            int success = invQrcodedetailService.updateBatchAtomic(invQrcodedetailEntityList);

            // 5. 返回结果
            return R.ok(true, i18nService.getMessage("batch.update.success", invQrcodedetailList.size()));

        } catch (Exception e) {
            // 事务会自动回滚
            return R.fail(i18nService.getMessage("batch.update.failed", e.getMessage()));
        }
    }
   
}
