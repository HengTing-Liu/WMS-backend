package com.abclonal.product.service.sys.impl;

import com.abclonal.product.dao.entity.Supplier;
import com.abclonal.product.dao.mapper.SupplierMapper;
import com.abclonal.product.service.sys.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public List<Supplier> list(Supplier condition) {
        return supplierMapper.selectList(condition);
    }

    @Override
    public Supplier getById(Long id) {
        Supplier supplier = supplierMapper.selectById(id);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        return supplier;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Supplier supplier) {
        if (supplierMapper.checkSupplierCodeExists(supplier.getSupplierCode(), null) > 0) {
            throw new RuntimeException("供应商编码已存在");
        }
        supplier.setDelFlag(0);
        supplier.setCreateBy("system");
        supplierMapper.insert(supplier);
        return supplier.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, Supplier supplier) {
        Supplier exist = supplierMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("供应商不存在");
        }
        if (!exist.getSupplierCode().equals(supplier.getSupplierCode()) &&
                supplierMapper.checkSupplierCodeExists(supplier.getSupplierCode(), id) > 0) {
            throw new RuntimeException("供应商编码已存在");
        }
        supplier.setId(id);
        supplier.setUpdateBy("system");
        int rows = supplierMapper.update(supplier);
        if (rows == 0) {
            throw new RuntimeException("更新失败，供应商不存在或已删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Supplier exist = supplierMapper.selectById(id);
        if (exist == null) {
            throw new RuntimeException("供应商不存在");
        }
        int rows = supplierMapper.deleteById(id);
        if (rows == 0) {
            throw new RuntimeException("删除失败");
        }
    }

    @Override
    public List<Supplier> listAll() {
        return supplierMapper.selectList(new Supplier());
    }

    @Override
    public List<Supplier> listByKeyword(String keyword) {
        return supplierMapper.selectByKeyword(keyword);
    }
}
