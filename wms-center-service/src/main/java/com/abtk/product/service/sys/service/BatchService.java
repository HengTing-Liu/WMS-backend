package com.abtk.product.service.sys.service;

import com.abtk.product.dao.entity.Batch;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 批次档案服务接口
 */
public interface BatchService {
    /**
     * 分页查询批次列表
     */
    List<Batch> list(Batch condition);

    /**
     * 查询所有批次
     */
    List<Batch> listAll();

    /**
     * 根据ID查询批次详情
     */
    Batch getById(Long id);

    /**
     * 根据物料ID查询批次列表
     */
    List<Batch> listByMaterialId(Long materialId);

    /**
     * 根据物料编码查询批次列表
     */
    List<Batch> listByMaterialCode(String materialCode);

    /**
     * 根据批次号查询批次
     */
    Batch getByBatchNo(String batchNo);

    /**
     * 新增批次
     */
    Long create(Batch batch);

    /**
     * 更新批次
     */
    void update(Long id, Batch batch);

    /**
     * 删除批次
     */
    void delete(Long id);

    /**
     * 联合查询物料和批次信息（用于物料批次查询页面）
     */
    List<Batch> listMaterialBatch(String materialCode, String materialName, String batchNo,
                                  String warehouseCode, String qcStatus);

    /**
     * 导出批次档案
     */
    void export(HttpServletResponse response, String materialCode, String materialName,
                String batchNo, String qcStatus);
}
