package com.abclonal.product.service.sys.service;

import com.abclonal.product.dao.entity.WarehouseReceiver;

import java.util.List;

/**
 * 仓库收货信息服务接口
 * Service层只做简单CRUD，复杂业务在Biz层处理
 */
public interface WarehouseReceiverService {

    /**
     * 根据仓库编码查询收货地址列表
     *
     * @param warehouseCode 仓库编码
     * @return 收货地址列表
     */
    List<WarehouseReceiver> list(String warehouseCode);

    /**
     * 根据ID查询收货地址详情
     *
     * @param id 收货地址ID
     * @return 收货地址详情
     */
    WarehouseReceiver getById(Long id);

    /**
     * 新增收货地址
     *
     * @param warehouseReceiver 收货地址信息（Entity）
     * @return 新增收货地址ID
     */
    Long create(WarehouseReceiver warehouseReceiver);

    /**
     * 更新收货地址
     *
     * @param id 收货地址ID
     * @param warehouseReceiver 收货地址信息（Entity）
     */
    void update(Long id, WarehouseReceiver warehouseReceiver);

    /**
     * 删除收货地址（逻辑删除）
     *
     * @param id 收货地址ID
     */
    void delete(Long id);

    /**
     * 设置默认收货地址
     * 会将同一仓库的其他地址设为非默认
     *
     * @param id 收货地址ID
     */
    void setDefault(Long id);
}
