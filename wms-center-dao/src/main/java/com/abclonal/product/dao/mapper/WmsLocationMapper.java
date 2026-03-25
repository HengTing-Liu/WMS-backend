package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.WmsLocation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库位档案表(WmsLocation)表数据库访问层
 *
 * @author wms
 * @since 2026-03-14
 */
public interface WmsLocationMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WmsLocation queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param wmsLocation 查询条件
     * @return 对象列表
     */
    List<WmsLocation> queryAll(WmsLocation wmsLocation);

    /**
     * 通过父ID查询子节点列表
     *
     * @param parentId 父ID
     * @return 子节点列表
     */
    List<WmsLocation> queryByParentId(@Param("parentId") Long parentId);

    /**
     * 查询所有根节点
     *
     * @return 根节点列表
     */
    List<WmsLocation> queryRootNodes();

    /**
     * 通过仓库编码查询
     *
     * @param warehouseCode 仓库编码
     * @return 对象列表
     */
    List<WmsLocation> queryByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    /**
     * 递归查询所有子节点
     *
     * @param parentId 父ID
     * @return 所有子节点列表
     */
    List<WmsLocation> queryAllChildren(@Param("parentId") Long parentId);

    /**
     * 递归查询子节点（限制层级）
     *
     * @param parentId 父ID
     * @param maxLevel 最大层级
     * @return 子节点列表
     */
    List<WmsLocation> queryChildrenByLevel(@Param("parentId") Long parentId, @Param("maxLevel") Integer maxLevel);

    /**
     * 统计总行数
     *
     * @param wmsLocation 查询条件
     * @return 总行数
     */
    long count(WmsLocation wmsLocation);

    /**
     * 新增数据
     *
     * @param wmsLocation 实例对象
     * @return 影响行数
     */
    int insert(WmsLocation wmsLocation);

    /**
     * 批量新增数据
     *
     * @param entities List<WmsLocation> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<WmsLocation> entities);

    /**
     * 批量新增或按主键更新数据
     *
     * @param entities List<WmsLocation> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<WmsLocation> entities);

    /**
     * 修改数据
     *
     * @param wmsLocation 实例对象
     * @return 影响行数
     */
    int update(WmsLocation wmsLocation);

    /**
     * 批量更新
     *
     * @param list 要更新的数据列表
     * @return 更新的记录数
     */
    int updateBatch(@Param("list") List<WmsLocation> list);

    /**
     * 更新已用容量
     *
     * @param id 主键
     * @param capacityUsed 已用容量
     * @return 影响行数
     */
    int updateCapacityUsed(@Param("id") Long id, @Param("capacityUsed") Integer capacityUsed);

    /**
     * 通过主键逻辑删除数据
     *
     * @param id 主键
     * @param username 操作人
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("username") String username);

    /**
     * 批量逻辑删除
     *
     * @param ids ID列表
     * @param username 操作人
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids, @Param("username") String username);

    /**
     * 递归逻辑删除（删除节点及其所有子节点）
     *
     * @param id 节点ID
     * @param username 操作人
     * @return 影响行数
     */
    int deleteRecursive(@Param("id") Long id, @Param("username") String username);

    /**
     * 查询已占用的子节点数量
     *
     * @param parentId 父ID
     * @return 已占用数量
     */
    int countUsedChildren(@Param("parentId") Long parentId);

    /**
     * 查询子节点总容量
     *
     * @param parentId 父ID
     * @return 总容量
     */
    int sumChildrenCapacity(@Param("parentId") Long parentId);

    /**
     * 查询子节点已用容量总和
     *
     * @param parentId 父ID
     * @return 已用容量
     */
    int sumChildrenUsedCapacity(@Param("parentId") Long parentId);
}
