package com.abclonal.product.service.sys.service;

import com.abclonal.product.dao.entity.Customer;

import java.util.List;

/**
 * 客户档案服务接口
 * Service层只做简单CRUD，复杂业务在Biz层处理
 */
public interface CustomerService {

    /**
     * 查询客户列表
     *
     * @param condition 查询条件（Entity）
     * @return 客户列表
     */
    List<Customer> list(Customer condition);

    /**
     * 根据ID查询客户详情
     *
     * @param id 客户ID
     * @return 客户详情
     */
    Customer getById(Long id);

    /**
     * 新增客户
     *
     * @param customer 客户信息（Entity）
     * @return 新增客户ID
     */
    Long create(Customer customer);

    /**
     * 更新客户
     *
     * @param id 客户ID
     * @param customer 客户信息（Entity）
     */
    void update(Long id, Customer customer);

    /**
     * 删除客户（逻辑删除）
     *
     * @param id 客户ID
     */
    void delete(Long id);

    /**
     * 切换客户状态
     *
     * @param id 客户ID
     * @param enabled 状态
     */
    void toggleStatus(Long id, Integer enabled);

    /**
     * 查询所有客户（不分页）
     *
     * @return 客户列表
     */
    List<Customer> listAll();
}
