package com.abtk.product.service.sys.impl;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.mapper.ColumnMetaMapper;
import com.abtk.product.dao.mapper.TableMetaMapper;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.sys.service.TableMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 表元数据服务实现
 * Service层只做简单CRUD，复杂业务在Biz层处理
 *
 * @author backend
 * @since 2026-04-06
 */
@Service
public class TableMetaServiceImpl implements TableMetaService {

    @Autowired
    private TableMetaMapper tableMetaMapper;

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Override
    public List<TableMeta> list(TableMeta condition) {
        return tableMetaMapper.selectList(condition);
    }

    @Override
    public TableMeta getById(Long id) {
        TableMeta tableMeta = tableMetaMapper.selectById(id);
        if (tableMeta == null) {
            throw new ServiceException("表元数据不存在");
        }
        return tableMeta;
    }

    @Override
    public TableMeta getByTableCode(String tableCode) {
        return tableMetaMapper.selectByTableCode(tableCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(TableMeta tableMeta) {
        // 检查表编码是否已存在
        TableMeta exist = tableMetaMapper.selectByTableCode(tableMeta.getTableCode());
        if (exist != null) {
            throw new ServiceException("表编码已存在");
        }

        // 设置默认值
        if (tableMeta.getPageSize() == null) {
            tableMeta.setPageSize(20);
        }
        if (tableMeta.getIsTree() == null) {
            tableMeta.setIsTree(0);
        }
        if (tableMeta.getShowCheckbox() == null) {
            tableMeta.setShowCheckbox(0);
        }
        if (tableMeta.getShowIndex() == null) {
            tableMeta.setShowIndex(1);
        }
        if (tableMeta.getStatus() == null) {
            tableMeta.setStatus(1);
        }

        // 设置创建人
        String username = SecurityUtils.getUsername();
        tableMeta.setCreateBy(username);

        tableMetaMapper.insert(tableMeta);
        return tableMeta.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, TableMeta tableMeta) {
        // 检查表元数据是否存在
        TableMeta exist = tableMetaMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("表元数据不存在");
        }

        // 检查表编码是否重复（排除当前ID）
        if (!exist.getTableCode().equals(tableMeta.getTableCode())) {
            TableMeta codeExist = tableMetaMapper.selectByTableCode(tableMeta.getTableCode());
            if (codeExist != null) {
                throw new ServiceException("表编码已存在");
            }
        }

        tableMeta.setId(id);

        // 设置更新人
        String username = SecurityUtils.getUsername();
        tableMeta.setUpdateBy(username);

        int rows = tableMetaMapper.update(tableMeta);
        if (rows == 0) {
            throw new ServiceException("更新失败，表元数据不存在或已删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 检查表元数据是否存在
        TableMeta exist = tableMetaMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("表元数据不存在");
        }

        // 检查是否有关联的字段
        List<?> columns = columnMetaMapper.selectByTableCode(exist.getTableCode());
        if (columns != null && !columns.isEmpty()) {
            throw new ServiceException("该表元数据下存在字段，请先删除关联字段");
        }

        int rows = tableMetaMapper.deleteById(id);
        if (rows == 0) {
            throw new ServiceException("删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer status) {
        // 检查表元数据是否存在
        TableMeta exist = tableMetaMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("表元数据不存在");
        }

        if (status != 0 && status != 1) {
            throw new ServiceException("状态值不合法");
        }

        TableMeta update = new TableMeta();
        update.setId(id);
        update.setStatus(status);
        update.setUpdateBy(SecurityUtils.getUsername());

        int rows = tableMetaMapper.update(update);
        if (rows == 0) {
            throw new ServiceException("状态切换失败");
        }
    }

    @Override
    public List<TableMeta> listAll() {
        return tableMetaMapper.selectAll();
    }

    @Override
    public List<TableMeta> listByModule(String module) {
        return tableMetaMapper.selectByModule(module);
    }
}
