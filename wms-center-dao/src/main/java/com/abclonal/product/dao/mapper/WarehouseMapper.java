package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.Warehouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库表 数据层
 */
public interface WarehouseMapper {

    /**
     * 根据ID查询仓库
     *
     * @param id 仓库ID
     * @return 仓库信息
     */
    Warehouse selectById(Long id);

    /**
     * 查询仓库列表（支持分页）
     *
     * @param warehouse 查询条件
     * @return 仓库集合
     */
    List<Warehouse> selectList(Warehouse warehouse);

    /**
     * 新增仓库
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    int insert(Warehouse warehouse);

    /**
     * 修改仓库
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    int update(Warehouse warehouse);

    /**
     * 逻辑删除仓库
     *
     * @param id 仓库ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 切换仓库状态
     *
     * @param id 仓库ID
     * @param isEnabled 状态
     * @return 结果
     */
    int updateStatus(@Param("id") Long id, @Param("isEnabled") Integer isEnabled);

    /**
     * 检查仓库编码是否已存在
     *
     * @param warehouseCode 仓库编码
     * @param excludeId 排除的ID（编辑时使用）
     * @return 数量
     */
    int checkWarehouseCodeExists(@Param("warehouseCode") String warehouseCode, @Param("excludeId") Long excludeId);

    /**
     * 查询所有不重复的公司列表
     *
     * @return 公司列表
     */
    List<String> selectDistinctCompany();
}
