package com.abtk.product.service.sys.impl;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.dao.entity.Customer;
import com.abtk.product.dao.mapper.CustomerMapper;
import com.abtk.product.service.sys.service.CustomerService;
import com.abtk.product.service.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户档案服务实现
 * Service层只做简单CRUD，复杂业务在Biz层处理
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> list(Customer condition) {
        return customerMapper.selectList(condition);
    }

    @Override
    public Customer getById(Long id) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new ServiceException("客户不存在");
        }
        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Customer customer) {
        // 检查客户编码是否已存在
        if (customerMapper.checkCustomerCodeExists(customer.getCustomerCode(), null) > 0) {
            throw new ServiceException("客户编码已存在");
        }

        customer.setIsDeleted(0);

        // 设置创建人
        String username = SecurityUtils.getUsername();
        customer.setCreateBy(username);

        customerMapper.insert(customer);
        return customer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, Customer customer) {
        // 检查客户是否存在
        Customer exist = customerMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("客户不存在");
        }

        // 检查客户编码是否重复（排除当前ID）
        if (!exist.getCustomerCode().equals(customer.getCustomerCode()) &&
                customerMapper.checkCustomerCodeExists(customer.getCustomerCode(), id) > 0) {
            throw new ServiceException("客户编码已存在");
        }

        customer.setId(id);

        // 设置更新人
        String username = SecurityUtils.getUsername();
        customer.setUpdateBy(username);

        int rows = customerMapper.update(customer);
        if (rows == 0) {
            throw new ServiceException("更新失败，客户不存在或已删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 检查客户是否存在
        Customer exist = customerMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("客户不存在");
        }

        int rows = customerMapper.deleteById(id);
        if (rows == 0) {
            throw new ServiceException("删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer enabled) {
        // 检查客户是否存在
        Customer exist = customerMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("客户不存在");
        }

        if (enabled != 0 && enabled != 1) {
            throw new ServiceException("状态值不合法");
        }

        int rows = customerMapper.updateStatus(id, enabled);
        if (rows == 0) {
            throw new ServiceException("状态切换失败");
        }
    }

    @Override
    public List<Customer> listAll() {
        Customer customer = new Customer();
        return customerMapper.selectAll(customer);
    }
}
