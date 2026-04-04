package com.abtk.product.dao.mapper;


import com.abtk.product.dao.entity.SysDictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表 数据层
 * 
 * @author ruoyi
 */
public interface SysDictTypeMapper
{
    /**
     * 根据条件分页查询字典类型
     * 
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * 根据所有字典类型
     * 
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * 根据字典类型ID查询信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    public SysDictType selectDictTypeById(Long dictId);

    /**
     * 根据字典类型查询信息
     * 
     * @param dictType 字典类型
     * @return 字典类型
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * 通过字典ID删除字典信息（逻辑删除）
     * 
     * @param dictId 字典ID
     * @param updateBy 更新人
     * @return 结果
     */
    public int deleteDictTypeById(@Param("dictId") Long dictId, @Param("updateBy") String updateBy);

    /**
     * 批量删除字典类型信息（逻辑删除）
     * 
     * @param dictIds 需要删除的字典ID
     * @param updateBy 更新人
     * @return 结果
     */
    public int deleteDictTypeByIds(@Param("dictIds") Long[] dictIds, @Param("updateBy") String updateBy);

    /**
     * 新增字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int insertDictType(SysDictType dictType);

    /**
     * 修改字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int updateDictType(SysDictType dictType);

    /**
     * 修改字典类型状态
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int updateDictTypeStatus(SysDictType dictType);

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dictType 字典类型
     * @return 结果
     */
    public SysDictType checkDictTypeUnique(String dictType);
}
