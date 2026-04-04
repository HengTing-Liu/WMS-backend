package com.abtk.product.service.sys.service;

import com.abtk.product.api.domain.response.sys.ColumnMetaVO;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.entity.TableOperation;

import java.util.List;

/**
 * 元数据服务接口
 */
public interface MetaService {

    /**
     * 获取表元数据（包含字段和操作）
     */
    TableMeta getTableMeta(String tableCode);

    /**
     * 查询字段元数据列表
     */
    List<ColumnMeta> getColumnMetaList(String tableCode);

    /**
     * 查询操作按钮列表
     */
    List<TableOperation> getOperationList(String tableCode);

    /**
     * 查询所有表元数据
     */
    List<TableMeta> listAllTables();

    /**
     * 按模块查询表元数据
     */
    List<TableMeta> listTablesByModule(String module);

    /**
     * 保存表元数据
     */
    TableMeta saveTableMeta(TableMeta tableMeta);

    /**
     * 保存字段元数据（先删除再插入）
     */
    void saveColumnMetaList(String tableCode, List<ColumnMeta> columns);

    /**
     * 删除表元数据
     */
    void deleteTableMeta(Long id);

    /**
     * 删除字段元数据
     */
    void deleteColumnMeta(Long id);

    /**
     * 删除操作按钮
     */
    void deleteOperation(Long id);

    /**
     * 获取字段 Schema VO 列表（前端友好格式）
     * @param tableCode 表标识
     * @return 字段 VO 列表
     */
    List<ColumnMetaVO> getColumnSchema(String tableCode);
}
