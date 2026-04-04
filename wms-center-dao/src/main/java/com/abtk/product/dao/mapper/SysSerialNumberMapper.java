package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.SysSerialNumber;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流水号规则表(SysSerialNumber)表数据库访问层
 *
 * @author makejava
 * @since 2026-03-09 13:30:00
 */
public interface SysSerialNumberMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysSerialNumber queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param sysSerialNumber 查询条件
     * @return 对象列表
     */
    List<SysSerialNumber> queryAll(SysSerialNumber sysSerialNumber);

    /**
     * 统计总行数
     *
     * @param sysSerialNumber 查询条件
     * @return 总行数
     */
    long count(SysSerialNumber sysSerialNumber);

    /**
     * 新增数据
     *
     * @param sysSerialNumber 实例对象
     * @return 影响行数
     */
    int insert(SysSerialNumber sysSerialNumber);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysSerialNumber> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysSerialNumber> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysSerialNumber> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysSerialNumber> entities);

    /**
     * 修改数据
     *
     * @param sysSerialNumber 实例对象
     * @return 影响行数
     */
    int update(SysSerialNumber sysSerialNumber);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @param updateBy 更新人
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") String updateBy);
    
    /**
     * 批量删除数据
     *
     * @param ids ID列表
     * @param updateBy 更新人
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids, @Param("updateBy") String updateBy);
    
    
    /**
     * 批量更新
     * @param list 要更新的数据列表
     * @return 更新的记录数
     */
    int updateBatch(@Param("list") List<SysSerialNumber> list);
}
