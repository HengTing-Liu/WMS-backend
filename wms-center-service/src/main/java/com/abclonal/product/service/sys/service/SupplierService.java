package com.abclonal.product.service.sys.service;

import com.abclonal.product.dao.entity.Supplier;
import java.util.List;

public interface SupplierService {
    List<Supplier> list(Supplier condition);
    Supplier getById(Long id);
    Long create(Supplier supplier);
    void update(Long id, Supplier supplier);
    void delete(Long id);
    List<Supplier> listAll();
    List<Supplier> listByKeyword(String keyword);
}
