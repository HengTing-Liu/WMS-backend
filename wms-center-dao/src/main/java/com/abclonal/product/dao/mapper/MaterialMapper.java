package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.Material;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MaterialMapper {
    Material selectById(Long id);
    List<Material> selectList(Material material);
    List<Material> selectExactList(Material material);
    List<Material> selectByKeyword(@Param("keyword") String keyword);
    int insert(Material material);
    int update(Material material);
    int deleteById(Long id);
    int checkMaterialCodeExists(@Param("materialCode") String materialCode, @Param("excludeId") Long excludeId);
}
