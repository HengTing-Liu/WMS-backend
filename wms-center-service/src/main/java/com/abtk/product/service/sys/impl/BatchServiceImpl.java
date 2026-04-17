package com.abtk.product.service.sys.impl;

import com.abtk.product.dao.entity.Batch;
import com.abtk.product.dao.mapper.BatchMapper;
import com.abtk.product.service.sys.service.BatchService;
import com.abtk.product.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchMapper batchMapper;

    @Override
    public List<Batch> list(Batch condition) {
        return batchMapper.selectList(condition);
    }

    @Override
    public List<Batch> listAll() {
        return batchMapper.selectAll();
    }

    @Override
    public Batch getById(Long id) {
        Batch batch = batchMapper.selectById(id);
        if (batch == null) {
            throw new RuntimeException("批次不存在");
        }
        return batch;
    }

    @Override
    public List<Batch> listByMaterialId(Long materialId) {
        return batchMapper.selectByMaterialId(materialId);
    }

    @Override
    public List<Batch> listByMaterialCode(String materialCode) {
        return batchMapper.selectByMaterialCode(materialCode);
    }

    @Override
    public Batch getByBatchNo(String batchNo) {
        return batchMapper.selectByBatchNo(batchNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Batch batch) {
        if (batchMapper.checkBatchNoExists(batch.getBatchNo(), null) > 0) {
            throw new RuntimeException("批次号已存在");
        }
        batch.setIsDeleted(0);
        batch.setCreateBy("system");
        batchMapper.insert(batch);
        return batch.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, Batch batch) {
        Batch exist = batchMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("批次不存在");
        }
        if (!exist.getBatchNo().equals(batch.getBatchNo()) &&
                batchMapper.checkBatchNoExists(batch.getBatchNo(), id) > 0) {
            throw new RuntimeException("批次号已存在");
        }
        batch.setId(id);
        batch.setUpdateBy("system");
        int rows = batchMapper.update(batch);
        if (rows == 0) {
            throw new RuntimeException("更新失败，批次不存在或已删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Batch exist = batchMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("批次不存在");
        }
        int rows = batchMapper.deleteById(id);
        if (rows == 0) {
            throw new RuntimeException("删除失败");
        }
    }

    @Override
    public List<Batch> listMaterialBatch(String materialCode, String materialName, String batchNo,
                                        String warehouseCode, String qcStatus) {
        return batchMapper.selectMaterialBatchList(materialCode, materialName, batchNo,
                warehouseCode, qcStatus);
    }

    @Override
    public void export(HttpServletResponse response, String materialCode, String materialName,
                      String batchNo, String qcStatus) {
        List<Batch> list = batchMapper.selectMaterialBatchList(materialCode, materialName, batchNo,
                null, qcStatus);
        ExcelUtil<Batch> util = new ExcelUtil<>(Batch.class);
        util.exportExcel(response, list, "批次数据");
    }
}
