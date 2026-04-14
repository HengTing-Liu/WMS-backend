package com.abtk.product.service.sys.service;

import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.api.domain.response.sys.ColumnMetaVO;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.FormGroupMeta;
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
     * 根据ID获取表元数据
     */
    TableMeta getById(Long id);

    /**
     * 根据编码获取表元数据
     */
    TableMeta getByCode(String tableCode);

    /**
     * 分页查询表元数据
     */
    List<TableMeta> listPage(TableMetaQueryRequest request);

    /**
     * 查询字段元数据列表
     */
    List<ColumnMeta> getColumnMetaList(String tableCode);

    /**
     * 根据ID查询字段元数据
     */
    ColumnMeta getColumnMetaById(Long id);

    /**
     * 查询表单分组元数据列表
     */
    List<FormGroupMeta> getFormGroupMetaList(String tableCode);

    /**
     * 根据ID查询表单分组元数据
     */
    FormGroupMeta getFormGroupMetaById(Long id);

    /**
     * 查询操作按钮列表
     */
    List<TableOperation> getOperationList(String tableCode);

    /**
     * 根据ID查询操作按钮
     */
    TableOperation getOperationById(Long id);

    /**
     * 保存操作按钮（新增或更新）
     */
    TableOperation saveOperation(TableOperation operation);

    /**
     * 批量更新操作按钮排序
     */
    void sortOperations(List<TableOperation> operations);

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
     * 新增字段元数据
     */
    void addColumnMeta(ColumnMeta column);

    /**
     * 更新字段元数据
     */
    void updateColumnMeta(ColumnMeta column);

    /**
     * 批量更新字段排序号
     */
    void batchUpdateColumnSort(List<ColumnMeta> columns);

    /**
     * 保存表单分组元数据
     */
    FormGroupMeta saveFormGroupMeta(FormGroupMeta formGroupMeta);

    /**
     * 批量更新表单分组排序
     */
    void batchUpdateFormGroupSort(List<FormGroupMeta> groups);

    /**
     * 删除表单分组元数据
     */
    void deleteFormGroupMeta(Long id);

    /**
     * 删除操作按钮
     */
    void deleteOperation(Long id);

    /**
     * 切换启用/禁用状态
     */
    void toggleStatus(Long id);

    /**
     * 批量删除操作按钮
     */
    void deleteOperations(List<Long> ids);

    /**
     * 获取字段 Schema VO 列表（前端友好格式）
     * @param tableCode 表标识
     * @return 字段 VO 列表
     */
    List<ColumnMetaVO> getColumnSchema(String tableCode);
}
