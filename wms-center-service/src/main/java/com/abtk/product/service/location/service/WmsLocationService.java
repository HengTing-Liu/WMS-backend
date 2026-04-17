package com.abtk.product.service.location.service;

import com.abtk.product.dao.entity.WmsLocation;

import java.util.List;
import java.util.Map;

/**
 * 库位档案表(WmsLocation)表服务接口
 *
 * @author wms
 * @since 2026-03-14
 */
public interface WmsLocationService {

    // ==================== 查询方法 ====================

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WmsLocation queryById(Long id);

    /**
     * 根据条件查询列表
     *
     * @param wmsLocation 筛选条件
     * @return 对象列表
     */
    List<WmsLocation> queryByCondition(WmsLocation wmsLocation);

    /**
     * 通过父ID查询子节点列表
     *
     * @param parentId 父ID
     * @return 子节点列表
     */
    List<WmsLocation> queryByParentId(Long parentId);

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
    List<WmsLocation> queryByWarehouseCode(String warehouseCode);

    /**
     * 递归查询所有子节点
     *
     * @param parentId 父ID
     * @return 所有子节点列表
     */
    List<WmsLocation> queryAllChildren(Long parentId);

    /**
     * 递归查询子节点（限制层级）
     *
     * @param parentId 父ID
     * @param maxLevel 最大层级
     * @return 子节点列表
     */
    List<WmsLocation> queryChildrenByLevel(Long parentId, Integer maxLevel);

    /**
     * 统计总行数
     *
     * @param wmsLocation 筛选条件
     * @return 总行数
     */
    long count(WmsLocation wmsLocation);

    /**
     * 判断数据是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    boolean existsById(Long id);

    // ==================== 新增方法 ====================

    /**
     * 新增单条数据
     *
     * @param wmsLocation 实例对象
     * @return 实例对象
     */
    WmsLocation insert(WmsLocation wmsLocation);

    /**
     * 批量新增
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    int insertBatchAtomic(List<WmsLocation> entities);

    /**
     * 批量新增或更新
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(List<WmsLocation> entities);

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param wmsLocation 实例对象
     * @return 影响行数
     */
    int update(WmsLocation wmsLocation);

    /**
     * 批量修改
     *
     * @param list 实例对象列表
     * @return 影响行数
     */
    int updateBatchAtomic(List<WmsLocation> list);

    // ==================== 删除方法 ====================

    /**
     * 通过主键逻辑删除数据
     *
     * @param id 主键
     * @param username 操作人
     * @return 是否成功
     */
    boolean logicDeleteById(Long id, String username);

    /**
     * 批量逻辑删除
     *
     * @param ids 主键列表
     * @param username 操作人
     * @return 影响行数
     */
    int logicDeleteBatchByIds(List<Long> ids, String username);

    /**
     * 递归逻辑删除（删除节点及其所有子节点）
     *
     * @param id 节点ID
     * @param username 操作人
     * @return 影响行数
     */
    int logicDeleteRecursive(Long id, String username);

    // ==================== 容量统计方法 ====================

    /**
     * 查询所有库位（不分页）
     *
     * @return 库位列表
     */
    List<WmsLocation> listAll();

    /**
     * 切换库位使用状态
     *
     * @param id 主键
     * @param enabled 是否启用：0=禁用，1=启用
     */
    void toggleStatus(Long id, Integer enabled);

    /**
     * 根据ID列表批量查询存储容器
     *
     * @param ids ID列表
     * @return 存储容器列表
     */
    List<WmsLocation> queryByIds(List<Long> ids);

    /**
     * 批量更新仓库编码
     *
     * @param ids 库位ID列表
     * @param warehouseCode 目标仓库编码
     * @param updateBy 更新人
     * @return 影响行数
     */
    int batchUpdateWarehouseCode(List<Long> ids, String warehouseCode, String updateBy);

    /**
     * 更新父节点的 internal_quantity（累加）
     *
     * @param parentId 父节点ID
     * @param delta 增量（可正可负）
     * @return 影响行数
     */
    int updateInternalQuantity(Long parentId, Integer delta);

    /**
     * 根据全路径名称查询库位
     *
     * @param warehouseCode 仓库编码
     * @param locationFullpathName 全路径名称
     * @return 库位列表
     */
    List<WmsLocation> queryByFullpathName(String warehouseCode, String locationFullpathName);

    /**
     * 查询仓库下同名库位数量
     *
     * @param warehouseCode 仓库编码
     * @param locationName 库位名称
     * @param parentId 父节点ID（可选）
     * @param excludeId 排除的ID（可选）
     * @return 数量
     */
    int countByNameAndWarehouse(String warehouseCode, String locationName, Long parentId, Long excludeId);

    /**
     * 检查库位是否可以删除
     *
     * @param id 库位ID
     * @return 包含 canDelete、childCount、usedCount 等信息的 Map
     */
    Map<String, Object> checkDelete(Long id);

    /**
     * 🔴 新增：查询占用状态（通过location_sort_no前缀匹配）
     * 用于删除校验性能优化
     *
     * @param locationSortNo 父节点排序号前缀
     * @return 占用数量
     */
    int countUsedBySortNoPrefix(String locationSortNo);

    /**
     * 统计子节点数量
     *
     * @param parentId 父节点ID
     * @return 子节点数量
     */
    int countChildren(Long parentId);

    /**
     * 统计已占用的子节点数量
     *
     * @param parentId 父节点ID
     * @return 已占用子节点数量
     */
    int countUsedChildren(Long parentId);
}
