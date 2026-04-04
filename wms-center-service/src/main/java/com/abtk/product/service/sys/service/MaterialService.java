package com.abtk.product.service.sys.service;

import com.abtk.product.dao.entity.Material;
import java.util.List;

public interface MaterialService {
    List<Material> list(Material condition);
    Material getById(Long id);
    Long create(Material material);
    void update(Long id, Material material);
    void delete(Long id);
    List<Material> listAll();
    List<Material> listByKeyword(String keyword);
}
