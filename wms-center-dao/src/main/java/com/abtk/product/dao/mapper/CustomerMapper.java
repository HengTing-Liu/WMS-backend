package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户表 数据层
 */
public interface CustomerMapper {

    /**
     * 根据ID查询客户
     *
     * @param id 客户ID
     * @return 客户信息
     */
    Customer selectById(Long id);

    /**
     * 查询客户列表（支持分页）
     *
     * @param customer 查询条件
     * @return 客户集合
     */
    List<Customer> selectList(Customer customer);

    /**
     * 查询所有客户（不分页）
     *
     * @param customer 查询条件
     * @return 客户集合
     */
    List<Customer> selectAll(Customer customer);

    /**
     * 新增客户
     *
     * @param customer 客户信息
     * @return 结果
     */
    int insert(Customer customer);

    /**
     * 修改客户
     *
     * @param customer 客户信息
     * @return 结果
     */
    int update(Customer customer);

    /**
     * 逻辑删除客户
     *
     * @param id 客户ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 切换客户状态
     *
     * @param id 客户ID
     * @param isEnabled 状态
     * @return 结果
     */
    int updateStatus(@Param("id") Long id, @Param("isEnabled") Integer isEnabled);

    /**
     * 检查客户编码是否已存在
     *
     * @param customerCode 客户编码
     * @param excludeId 排除的ID（编辑时使用）
     * @return 数量
     */
    int checkCustomerCodeExists(@Param("customerCode") String customerCode, @Param("excludeId") Long excludeId);
}
