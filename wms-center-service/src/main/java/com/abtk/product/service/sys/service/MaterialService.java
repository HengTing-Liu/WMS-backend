package com.abtk.product.service.sys.service;

import com.abtk.product.dao.entity.Material;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MaterialService {
    List<Material> list(Material condition);
    Material getById(Long id);
    Long create(Material material);
    void update(Long id, Material material);
    void delete(Long id);
    List<Material> listAll();
    List<Material> listByKeyword(String keyword);
    void toggleStatus(Long id, Integer enabled);
    void export(HttpServletResponse response, String materialCode, String materialName, String category, Integer status);
}
