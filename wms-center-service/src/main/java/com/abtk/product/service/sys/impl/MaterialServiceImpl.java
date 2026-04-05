package com.abtk.product.service.sys.impl;

import com.abtk.product.dao.entity.Material;
import com.abtk.product.dao.mapper.MaterialMapper;
import com.abtk.product.service.sys.service.MaterialService;
import com.abtk.product.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public List<Material> list(Material condition) {
        return materialMapper.selectList(condition);
    }

    @Override
    public Material getById(Long id) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            throw new RuntimeException("物料不存在");
        }
        return material;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Material material) {
        if (materialMapper.checkMaterialCodeExists(material.getMaterialCode(), null) > 0) {
            throw new RuntimeException("物料编码已存在");
        }
        material.setDelFlag(0);
        material.setCreateBy("system");
        materialMapper.insert(material);
        return material.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, Material material) {
        Material exist = materialMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("物料不存在");
        }
        if (!exist.getMaterialCode().equals(material.getMaterialCode()) &&
                materialMapper.checkMaterialCodeExists(material.getMaterialCode(), id) > 0) {
            throw new RuntimeException("物料编码已存在");
        }
        material.setId(id);
        material.setUpdateBy("system");
        int rows = materialMapper.update(material);
        if (rows == 0) {
            throw new RuntimeException("更新失败，物料不存在或已删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Material exist = materialMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("物料不存在");
        }
        int rows = materialMapper.deleteById(id);
        if (rows == 0) {
            throw new RuntimeException("删除失败");
        }
    }

    @Override
    public List<Material> listAll() {
        return materialMapper.selectList(new Material());
    }

    @Override
    public List<Material> listByKeyword(String keyword) {
        return materialMapper.selectByKeyword(keyword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer enabled) {
        Material material = new Material();
        material.setId(id);
        material.setStatus(enabled);
        int rows = materialMapper.updateStatus(id, enabled);
        if (rows == 0) {
            throw new RuntimeException("切换状态失败，物料不存在");
        }
    }

    @Override
    public void export(HttpServletResponse response, String materialCode, String materialName, String category, Integer status) {
        Material condition = new Material();
        condition.setMaterialCode(materialCode);
        condition.setMaterialName(materialName);
        condition.setCategory(category);
        condition.setStatus(status);
        List<Material> list = materialMapper.selectList(condition);
        ExcelUtil<Material> util = new ExcelUtil<>(Material.class);
        util.exportExcel(response, list, "物料档案");
    }
}
