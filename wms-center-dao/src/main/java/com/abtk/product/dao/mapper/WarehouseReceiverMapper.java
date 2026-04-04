package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.WarehouseReceiver;

import java.util.List;

/**
 * 仓库收货信息表 数据层
 */
public interface WarehouseReceiverMapper {

    /**
     * 根据ID查询收货地址
     *
     * @param id 收货地址ID
     * @return 收货地址信息
     */
    WarehouseReceiver selectById(Long id);

    /**
     * 根据仓库编码查询收货地址列表
     *
     * @param warehouseCode 仓库编码
     * @return 收货地址集合
     */
    List<WarehouseReceiver> selectByWarehouseCode(String warehouseCode);

    /**
     * 新增收货地址
     *
     * @param warehouseReceiver 收货地址信息
     * @return 结果
     */
    int insert(WarehouseReceiver warehouseReceiver);

    /**
     * 修改收货地址
     *
     * @param warehouseReceiver 收货地址信息
     * @return 结果
     */
    int update(WarehouseReceiver warehouseReceiver);

    /**
     * 逻辑删除收货地址
     *
     * @param id 收货地址ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 将指定仓库的所有地址设为非默认
     *
     * @param warehouseCode 仓库编码
     * @return 结果
     */
    int clearDefaultByWarehouseCode(String warehouseCode);

    /**
     * 设置默认地址
     *
     * @param id 收货地址ID
     * @return 结果
     */
    int setDefault(Long id);
}
