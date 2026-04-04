package com.abtk.product.biz.sys;

import com.abtk.product.api.domain.request.sys.CustomerQueryRequest;
import com.abtk.product.api.domain.request.sys.CustomerRequest;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.Customer;
import com.abtk.product.domain.converter.CustomerConverter;
import com.abtk.product.service.sys.service.CustomerService;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 客户档案业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-26
 */
@Slf4j
@Component
public class CustomerBiz {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private I18nService i18nService;

    /**
     * 查询客户列表
     */
    public R<List<Customer>> list(CustomerQueryRequest queryRequest) {
        Customer condition = new Customer();
        BeanUtils.copyProperties(queryRequest, condition);
        List<Customer> list = customerService.list(condition);
        return R.ok(list);
    }

    /**
     * 查询所有客户
     */
    public R<List<Customer>> listAll() {
        List<Customer> list = customerService.listAll();
        return R.ok(list);
    }

    /**
     * 根据ID查询客户详情
     */
    public R<Customer> queryById(Long id) {
        Customer entity = customerService.getById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.customer.not.found"));
        }
        return R.ok(entity);
    }

    /**
     * 新增客户
     */
    public R<Long> add(CustomerRequest request) {
        // 使用Converter转换Request到Entity
        Customer customer = CustomerConverter.INSTANCE.requestToEntity(request);

        Long id = customerService.create(customer);
        return R.ok(id);
    }

    /**
     * 更新客户
     */
    public R<Void> update(Long id, CustomerRequest request) {
        // 使用Converter转换Request到Entity
        Customer customer = CustomerConverter.INSTANCE.requestToEntity(request);

        customerService.update(id, customer);
        return R.ok();
    }

    /**
     * 删除客户
     */
    public R<Void> delete(Long id) {
        customerService.delete(id);
        return R.ok();
    }

    /**
     * 切换客户状态
     */
    public R<Void> toggleStatus(Long id, Integer enabled) {
        customerService.toggleStatus(id, enabled);
        return R.ok();
    }
}
