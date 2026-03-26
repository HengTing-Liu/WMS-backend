package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.Supplier;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SupplierMapper {
    Supplier selectById(Long id);
    List<Supplier> selectList(Supplier supplier);
    List<Supplier> selectExactList(Supplier supplier);
    List<Supplier> selectByKeyword(@Param("keyword") String keyword);
    int insert(Supplier supplier);
    int update(Supplier supplier);
    int deleteById(Long id);
    int checkSupplierCodeExists(@Param("supplierCode") String supplierCode, @Param("excludeId") Long excludeId);
}
