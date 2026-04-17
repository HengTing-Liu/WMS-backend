package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.Batch;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 批次档案 Mapper 接口
 */
public interface BatchMapper {
    /**
     * 根据ID查询批次
     */
    Batch selectById(Long id);

    /**
     * 条件查询批次列表
     */
    List<Batch> selectList(Batch batch);

    /**
     * 查询所有批次列表
     */
    List<Batch> selectAll();

    /**
     * 根据物料ID查询批次
     */
    List<Batch> selectByMaterialId(@Param("materialId") Long materialId);

    /**
     * 根据物料编码查询批次
     */
    List<Batch> selectByMaterialCode(@Param("materialCode") String materialCode);

    /**
     * 根据批次号查询批次
     */
    Batch selectByBatchNo(@Param("batchNo") String batchNo);

    /**
     * 新增批次
     */
    int insert(Batch batch);

    /**
     * 更新批次
     */
    int update(Batch batch);

    /**
     * 删除批次（逻辑删除）
     */
    int deleteById(Long id);

    /**
     * 检查批次号是否存在
     */
    int checkBatchNoExists(@Param("batchNo") String batchNo, @Param("excludeId") Long excludeId);

    /**
     * 联合查询物料和批次信息（用于物料批次查询页面）
     */
    List<Batch> selectMaterialBatchList(@Param("materialCode") String materialCode,
                                        @Param("materialName") String materialName,
                                        @Param("batchNo") String batchNo,
                                        @Param("warehouseCode") String warehouseCode,
                                        @Param("qcStatus") String qcStatus);
}
