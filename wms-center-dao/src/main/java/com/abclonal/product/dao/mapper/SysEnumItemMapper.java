package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.SysEnumItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 枚举明细表(SysEnumItem)表数据库访问层
 *
 * @author makejava
 * @since 2026-03-06 16:06:55
 */
public interface SysEnumItemMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysEnumItem queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param sysEnumItem 查询条件
     * @return 对象列表
     */
    List<SysEnumItem> queryAll(SysEnumItem sysEnumItem);

    /**
     * 统计总行数
     *
     * @param sysEnumItem 查询条件
     * @return 总行数
     */
    long count(SysEnumItem sysEnumItem);

    /**
     * 新增数据
     *
     * @param sysEnumItem 实例对象
     * @return 影响行数
     */
    int insert(SysEnumItem sysEnumItem);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysEnumItem> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysEnumItem> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysEnumItem> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysEnumItem> entities);

    /**
     * 修改数据
     *
     * @param sysEnumItem 实例对象
     * @return 影响行数
     */
    int update(SysEnumItem sysEnumItem);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id,String username);
    
    /**
     * 通过主键删除数据
     *
     * @param ids 数组
     * @return 影响行数
     */
    int deleteBatchByIds(List<Long> ids,String username);
    
    
        /**
     * 批量更新
     * @param list 要更新的数据列表
     * @return 更新的记录数
     */
    int updateBatch(@Param("list") List<SysEnumItem> list);
}

