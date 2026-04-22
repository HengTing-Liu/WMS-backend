package com.abtk.product.service.sys.service;

import com.abtk.product.dao.entity.Warehouse;

import java.util.List;

/**
 * 仓库档案服务接口
 * Service层只做简单CRUD，复杂业务在Biz层处理
 */
public interface WarehouseService {

    /**
     * 查询仓库列表
     *
     * @param condition 查询条件（Entity）
     * @return 仓库列表
     */
    List<Warehouse> list(Warehouse condition);

    /**
     * 根据ID查询仓库详情
     *
     * @param id 仓库ID
     * @return 仓库详情
     */
    Warehouse getById(Long id);

    /**
     * 新增仓库
     *
     * @param warehouse 仓库信息（Entity）
     * @return 新增仓库ID
     */
    Long create(Warehouse warehouse);

    /**
     * 更新仓库
     *
     * @param id 仓库ID
     * @param warehouse 仓库信息（Entity）
     */
    void update(Long id, Warehouse warehouse);

    /**
     * 删除仓库（逻辑删除）
     *
     * @param id 仓库ID
     */
    void delete(Long id);

    /**
     * 切换仓库状态
     *
     * @param id 仓库ID
     * @param enabled 状态
     */
    void toggleStatus(Long id, Integer enabled);

    /**
     * 查询所有仓库（不分页）
     *
     * @return 仓库列表
     */
    List<Warehouse> listAll();

    /**
     * 查询所有不重复的公司列表
     *
     * @return 公司列表
     */
    List<String> listDistinctCompany();

    /**
     * 根据仓库编码查询仓库信息
     *
     * @param warehouseCode 仓库编码
     * @return 仓库信息
     */
    Warehouse getByWarehouseCode(String warehouseCode);

    /**
     * 查询可选仓库列表（温区、责任部门、仓库类型、存储物料均一致）
     *
     * @param temperatureZone 温区
     * @param deptNameFullPath 责任部门全流程名称
     * @param warehouseType 仓库类型
     * @param storedMaterial 存储物料
     * @return 仓库列表
     */
    List<Warehouse> listByTemperatureZoneAndTypes(String temperatureZone, String deptNameFullPath, String warehouseType, String storedMaterial);

    /**
     * 根据仓库编码列表批量查询仓库
     *
     * @param warehouseCodes 仓库编码列表
     * @return 仓库列表
     */
    List<Warehouse> listByWarehouseCodes(List<String> warehouseCodes);
}
