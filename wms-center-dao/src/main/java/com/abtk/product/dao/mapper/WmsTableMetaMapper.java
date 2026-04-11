package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.WmsTableMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表元数据管理表(WmsTableMeta)表数据库访问层
 *
 * @author backend
 * @since 2026-04-06
 */
public interface WmsTableMetaMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WmsTableMeta queryById(Long id);

    /**
     * 通过表编码查询单条数据
     *
     * @param tableCode 表编码
     * @return 实例对象
     */
    WmsTableMeta queryByCode(@Param("tableCode") String tableCode);

    /**
     * 根据条件查询列表
     *
     * @param wmsTableMeta 查询条件
     * @return 对象列表
     */
    List<WmsTableMeta> queryAll(WmsTableMeta wmsTableMeta);

    /**
     * 查询所有数据（不分页）
     *
     * @return 所有数据
     */
    List<WmsTableMeta> listAll();

    /**
     * 统计总行数
     *
     * @param wmsTableMeta 查询条件
     * @return 总行数
     */
    long count(WmsTableMeta wmsTableMeta);

    /**
     * 新增数据
     *
     * @param wmsTableMeta 实例对象
     * @return 影响行数
     */
    int insert(WmsTableMeta wmsTableMeta);

    /**
     * 修改数据
     *
     * @param wmsTableMeta 实例对象
     * @return 影响行数
     */
    int update(WmsTableMeta wmsTableMeta);

    /**
     * 通过主键逻辑删除数据
     *
     * @param id 主键
     * @param username 操作人
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("username") String username);

    /**
     * 切换状态
     *
     * @param id 主键
     * @param status 状态：0=禁用，1=启用
     * @return 影响行数
     */
    int toggleStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 校验表编码唯一性
     *
     * @param tableCode 表编码
     * @return 结果
     */
    WmsTableMeta checkTableCodeUnique(@Param("tableCode") String tableCode);

    /**
     * 检查表是否有关联字段
     *
     * @param tableId 表ID
     * @return 关联字段数量
     */
    long countFieldByTableId(@Param("tableId") Long tableId);
}
