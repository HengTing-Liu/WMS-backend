package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.Storage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库区表 数据层
 * WMS0050 库区管理
 */
public interface StorageMapper {

    /**
     * 根据ID查询库区
     *
     * @param id 库区ID
     * @return 库区信息
     */
    Storage selectById(Long id);

    /**
     * 查询库区列表（支持分页）
     *
     * @param storage 查询条件
     * @return 库区集合
     */
    List<Storage> selectList(Storage storage);

    /**
     * 查询所有库区（不分页）
     *
     * @param storage 查询条件
     * @return 库区集合
     */
    List<Storage> selectAll(Storage storage);

    /**
     * 新增库区
     *
     * @param storage 库区信息
     * @return 结果
     */
    int insert(Storage storage);

    /**
     * 修改库区
     *
     * @param storage 库区信息
     * @return 结果
     */
    int update(Storage storage);

    /**
     * 逻辑删除库区
     *
     * @param id 库区ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 切换库区状态
     *
     * @param id 库区ID
     * @param isEnabled 状态
     * @return 结果
     */
    int updateStatus(@Param("id") Long id, @Param("isEnabled") Integer isEnabled);

    /**
     * 检查库区编码是否已存在
     *
     * @param storageCode 库区编码
     * @param excludeId 排除的ID（编辑时使用）
     * @return 数量
     */
    int checkStorageCodeExists(@Param("storageCode") String storageCode, @Param("excludeId") Long excludeId);
}
