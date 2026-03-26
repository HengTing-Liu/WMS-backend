package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.WmsDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典管理表(WmsDict)表数据库访问层
 *
 * @author backend
 * @since 2026-03-26
 */
public interface WmsDictMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WmsDict queryById(Long id);

    /**
     * 根据条件查询列表
     *
     * @param wmsDict 查询条件
     * @return 对象列表
     */
    List<WmsDict> queryAll(WmsDict wmsDict);

    /**
     * 查询所有数据（不分页）
     *
     * @return 所有数据
     */
    List<WmsDict> listAll();

    /**
     * 统计总行数
     *
     * @param wmsDict 查询条件
     * @return 总行数
     */
    long count(WmsDict wmsDict);

    /**
     * 新增数据
     *
     * @param wmsDict 实例对象
     * @return 影响行数
     */
    int insert(WmsDict wmsDict);

    /**
     * 修改数据
     *
     * @param wmsDict 实例对象
     * @return 影响行数
     */
    int update(WmsDict wmsDict);

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
     * @param isEnabled 是否启用：0=禁用，1=启用
     * @return 影响行数
     */
    int toggleStatus(@Param("id") Long id, @Param("isEnabled") Integer isEnabled);

    /**
     * 校验字典编码唯一性
     *
     * @param dictCode 字典编码
     * @return 结果
     */
    WmsDict checkDictCodeUnique(@Param("dictCode") String dictCode);
}
