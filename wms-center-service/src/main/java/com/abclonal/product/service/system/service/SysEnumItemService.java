package com.abclonal.product.service.system.service;

import com.abclonal.product.dao.entity.SysEnumItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;

/**
 * 枚举明细表(SysEnumItem)表服务接口
 *
 * @author lht
 * @since 2026-03-06 16:21:51
 */
public interface SysEnumItemService {

    // ==================== 查询方法 ====================
    
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysEnumItem queryById(Long id);

    /**
     * 根据条件查询列表
     *
     * @param sysEnumItem 筛选条件
     * @return 对象列表
     */
    List<SysEnumItem> queryByCondition(SysEnumItem sysEnumItem);


    /**
     * 统计总行数
     *
     * @param sysEnumItem 筛选条件
     * @return 总行数
     */
    long count(SysEnumItem sysEnumItem);

    // ==================== 新增方法 ====================

    /**
     * 新增单条数据
     *
     * @param sysEnumItem 实例对象
     * @return 实例对象
     */
    SysEnumItem insert(SysEnumItem sysEnumItem);
    /**
     * 新增单条数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    int insertBatchAtomic(List<SysEnumItem> entities);

    /**
     * 批量新增或更新
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(List<SysEnumItem> entities);

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param sysEnumItem 实例对象
     * @return 实例对象
     */
    int update(SysEnumItem sysEnumItem);


    // ==================== 删除方法 ====================
    /**
     * 通过主键逻辑删除数据（假删除）
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean logicDeleteById(Long id,String username);

    /**
     * 批量逻辑删除数据（假删除）
     *
     * @param ids 主键列表
     * @return 影响行数
     */
    int logicDeleteBatchByIds(List<Long> ids,String username);

    /**
     * 判断数据是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    boolean existsById(Long id);
    
      /**
     * 批量修改
     *
     * @param list 实例对象
     * @return 实例对象
     */
    int updateBatchAtomic(List<SysEnumItem> list);
    
}
