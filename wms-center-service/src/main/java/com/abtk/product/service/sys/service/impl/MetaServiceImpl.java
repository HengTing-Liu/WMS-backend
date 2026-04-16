package com.abtk.product.service.sys.service.impl;

import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.api.domain.response.sys.ColumnMetaVO;
import com.abtk.product.api.domain.response.sys.DictOptionVO;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.FormGroupMeta;
import com.abtk.product.dao.entity.SysDictData;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.entity.TableOperation;
import com.abtk.product.dao.mapper.ColumnMetaMapper;
import com.abtk.product.dao.mapper.DynamicMapper;
import com.abtk.product.dao.mapper.FormGroupMetaMapper;
import com.abtk.product.dao.mapper.SysDictDataMapper;
import com.abtk.product.dao.mapper.TableMetaMapper;
import com.abtk.product.dao.mapper.TableOperationMapper;
import com.abtk.product.service.sys.cache.MetaCacheEvent;
import com.abtk.product.service.sys.cache.MetaCacheService;
import com.abtk.product.service.sys.service.MetaService;
import com.abtk.product.service.system.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 元数据服务实现
 * <p>
 * 集成缓存机制：
 * - 变更时通过 ApplicationEventPublisher 发布 MetaCacheEvent
 * - MetaCacheEventListener 异步刷新缓存
 */
@Service
public class MetaServiceImpl implements MetaService {

    private static final Logger log = LoggerFactory.getLogger(MetaServiceImpl.class);

    @Autowired
    private TableMetaMapper tableMetaMapper;

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Autowired
    private FormGroupMetaMapper formGroupMetaMapper;

    @Autowired
    private TableOperationMapper tableOperationMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private I18nService i18nService;

    @Autowired(required = false)
    private MetaCacheService metaCacheService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public TableMeta getById(Long id) {
        TableMeta tableMeta = tableMetaMapper.selectById(id);
        if (tableMeta == null) {
            throw new ServiceException(i18nService.getMessage("meta.table.not.found", id.toString()));
        }
        return tableMeta;
    }

    @Override
    public TableMeta getByCode(String tableCode) {
        TableMeta tableMeta = tableMetaMapper.selectByTableCode(tableCode);
        if (tableMeta == null) {
            throw new ServiceException(i18nService.getMessage("meta.table.not.found", tableCode));
        }
        return tableMeta;
    }

    @Override
    public List<TableMeta> listPage(TableMetaQueryRequest request) {
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("tableCode", request.getTableCode());
        params.put("tableName", request.getTableName());
        params.put("module", request.getModule());
        params.put("status", request.getStatus());
        return tableMetaMapper.selectPage(params);
    }

    @Override
    public TableMeta getTableMeta(String tableCode) {
        // 优先从缓存读取
        if (metaCacheService != null) {
            try {
                Object cached = metaCacheService.getTableMeta(tableCode);
                if (cached instanceof TableMeta) {
                    return (TableMeta) cached;
                }
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                log.warn("缓存读取失败，降级到数据库查询 tableCode={}", tableCode, e);
            }
        }

        // 缓存未命中，从数据库查询
        TableMeta tableMeta = tableMetaMapper.selectByTableCode(tableCode);
        if (tableMeta == null) {
            throw new ServiceException(i18nService.getMessage("meta.table.not.found", tableCode));
        }
        // 从数据库查询真实字段信息
        try {
            List<Map<String, Object>> rows = dynamicMapper.selectTableColumns(tableCode);
            List<ColumnMeta> columns = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                ColumnMeta col = new ColumnMeta();
                col.setColumnName((String) row.get("column_name"));
                col.setColumnType((String) row.get("column_type"));
                col.setPrimaryKey("PRI".equals(row.get("column_key")));
                col.setNullable("YES".equals(row.get("is_nullable")));
                Object len = row.get("character_maximum_length");
                if (len == null) len = row.get("numeric_precision");
                col.setColumnSize(len != null ? ((Number) len).intValue() : 0);
                Object scale = row.get("numeric_scale");
                col.setDecimalDigits(scale != null ? ((Number) scale).intValue() : 0);
                columns.add(col);
            }
            tableMeta.setColumns(columns);
            log.info("成功加载表字段信息, tableCode={}, columnCount={}", tableCode, columns.size());
        } catch (Exception e) {
            log.error("查询表字段信息失败: tableCode={}", tableCode, e);
        }
        return tableMeta;
    }

    @Override
    public List<ColumnMeta> getColumnMetaList(String tableCode) {
        return columnMetaMapper.selectByTableCode(tableCode);
    }

    @Override
    public ColumnMeta getColumnMetaById(Long id) {
        return columnMetaMapper.selectById(id);
    }

    @Override
    public List<FormGroupMeta> getFormGroupMetaList(String tableCode) {
        return formGroupMetaMapper.selectByTableCode(tableCode);
    }

    @Override
    public FormGroupMeta getFormGroupMetaById(Long id) {
        return formGroupMetaMapper.selectById(id);
    }

    @Override
    public List<TableOperation> getOperationList(String tableCode) {
        return tableOperationMapper.selectByTableCode(tableCode);
    }

    @Override
    public TableOperation getOperationById(Long id) {
        return tableOperationMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TableOperation saveOperation(TableOperation operation) {
        if (operation.getId() == null) {
            // 新增
            tableOperationMapper.insert(operation);
        } else {
            // 更新
            tableOperationMapper.update(operation);
        }
        return operation;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sortOperations(List<TableOperation> operations) {
        for (TableOperation op : operations) {
            if (op.getId() != null && op.getSortOrder() != null) {
                tableOperationMapper.updateSortOrder(op.getId(), op.getSortOrder());
            }
        }
    }

    @Override
    public List<TableMeta> listAllTables() {
        return tableMetaMapper.selectAll();
    }

    @Override
    public List<TableMeta> listTablesByModule(String module) {
        return tableMetaMapper.selectByModule(module);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TableMeta saveTableMeta(TableMeta tableMeta) {
        String tableCode = tableMeta.getTableCode();
        // 表编码唯一性校验
        int count = tableMetaMapper.selectCountByTableCodeExcludeId(tableCode, tableMeta.getId());
        if (count > 0) {
            throw new ServiceException("表编码已存在: " + tableCode);
        }
        if (tableMeta.getId() == null) {
            tableMeta.setCreateBy("system");
            tableMeta.setCreateTime(new Date());
            tableMetaMapper.insert(tableMeta);
        } else {
            tableMeta.setUpdateBy("system");
            tableMeta.setUpdateTime(new Date());
            tableMetaMapper.update(tableMeta);
        }

        // 发布缓存变更事件（异步刷新缓存）
        if (tableCode != null) {
            eventPublisher.publishEvent(new MetaCacheEvent(this, tableCode, MetaCacheEvent.ChangeType.TABLE_UPDATED));
        }
        return tableMeta;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveColumnMetaList(String tableCode, List<ColumnMeta> columns) {
        columnMetaMapper.deleteByTableCode(tableCode);
        for (ColumnMeta column : columns) {
            column.setTableCode(tableCode);
            columnMetaMapper.insert(column);
        }

        // 发布字段变更事件
        eventPublisher.publishEvent(new MetaCacheEvent(this, tableCode, MetaCacheEvent.ChangeType.COLUMN_UPDATED));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTableMeta(Long id) {
        // 先查询获取 tableCode（用于缓存清理）
        TableMeta existing = tableMetaMapper.selectById(id);
        if (existing == null) {
            throw new ServiceException("表元数据不存在: " + id);
        }
        // 级联删除关联配置，保证前端“删除表元数据”可一次完成
        columnMetaMapper.deleteByTableCode(existing.getTableCode());
        formGroupMetaMapper.deleteByTableCode(existing.getTableCode());
        tableOperationMapper.deleteByTableCode(existing.getTableCode());
        tableMetaMapper.deleteById(id);

        // 发布删除事件
        if (existing.getTableCode() != null) {
            eventPublisher.publishEvent(new MetaCacheEvent(this, existing.getTableCode(), MetaCacheEvent.ChangeType.TABLE_DELETED));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id) {
        TableMeta existing = tableMetaMapper.selectById(id);
        if (existing == null) {
            throw new ServiceException("表元数据不存在: " + id);
        }
        TableMeta update = new TableMeta();
        update.setId(id);
        update.setStatus(existing.getStatus() != null && existing.getStatus() == 1 ? 0 : 1);
        update.setUpdateBy("system");
        update.setUpdateTime(new Date());
        tableMetaMapper.update(update);
        eventPublisher.publishEvent(new MetaCacheEvent(this, existing.getTableCode(), MetaCacheEvent.ChangeType.TABLE_UPDATED));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteColumnMeta(Long id) {
        columnMetaMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addColumnMeta(ColumnMeta column) {
        columnMetaMapper.insert(column);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnMeta(ColumnMeta column) {
        columnMetaMapper.update(column);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateColumnSort(List<ColumnMeta> columns) {
        for (ColumnMeta col : columns) {
            if (col.getId() != null && col.getSortOrder() != null) {
                columnMetaMapper.updateSortOrder(col.getId(), col.getSortOrder());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateColumnColSpan(List<Long> ids, Integer colSpan) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        columnMetaMapper.batchUpdateColSpanByIds(ids, colSpan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateColumnSection(List<Long> ids, String sectionKey, String sectionTitle,
                                         Integer sectionOrder, String sectionType, Integer sectionOpen) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        columnMetaMapper.batchUpdateSectionByIds(ids, sectionKey, sectionTitle, sectionOrder, sectionType, sectionOpen);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnSectionByGroupCode(String tableCode, String oldGroupCode, String newGroupCode) {
        if (tableCode == null || oldGroupCode == null || newGroupCode == null || oldGroupCode.equals(newGroupCode)) {
            return;
        }
        columnMetaMapper.updateSectionKeyByGroupCode(tableCode, oldGroupCode, newGroupCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FormGroupMeta saveFormGroupMeta(FormGroupMeta formGroupMeta) {
        if (formGroupMeta.getId() == null) {
            formGroupMeta.setCreateBy("system");
            formGroupMeta.setCreateTime(new Date());
            formGroupMetaMapper.insert(formGroupMeta);
        } else {
            // 检查 groupCode 是否变更，若变更则级联更新字段的 section_key
            FormGroupMeta existing = formGroupMetaMapper.selectById(formGroupMeta.getId());
            String oldGroupCode = existing != null ? existing.getGroupCode() : null;
            formGroupMeta.setUpdateBy("system");
            formGroupMeta.setUpdateTime(new Date());
            formGroupMetaMapper.update(formGroupMeta);
            if (oldGroupCode != null && !oldGroupCode.equals(formGroupMeta.getGroupCode())) {
                updateColumnSectionByGroupCode(formGroupMeta.getTableCode(), oldGroupCode, formGroupMeta.getGroupCode());
            }
        }
        return formGroupMeta;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateFormGroupSort(List<FormGroupMeta> groups) {
        for (FormGroupMeta group : groups) {
            if (group.getId() != null && group.getSortOrder() != null) {
                formGroupMetaMapper.updateSortOrder(group.getId(), group.getSortOrder());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFormGroupMeta(Long id) {
        formGroupMetaMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOperation(Long id) {
        tableOperationMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOperations(List<Long> ids) {
        if (ids != null) {
            for (Long id : ids) {
                tableOperationMapper.deleteById(id);
            }
        }
    }

    @Override
    public List<ColumnMetaVO> getColumnSchema(String tableCode) {
        List<ColumnMeta> columns = columnMetaMapper.selectByTableCode(tableCode);
        if (columns == null || columns.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return columns.stream().map(col -> {
            // 当 dictType 不为空时，查询字典数据
            List<DictOptionVO> options = null;
            if (col.getDictType() != null && !col.getDictType().isEmpty()) {
                try {
                    List<SysDictData> dictDatas = sysDictDataMapper.selectDictDataByType(col.getDictType());
                    if (dictDatas != null && !dictDatas.isEmpty()) {
                        options = dictDatas.stream().map(d ->
                            DictOptionVO.builder()
                                .value(d.getDictValue())
                                .label(d.getDictLabel())
                                .build()
                        ).collect(Collectors.toList());
                    }
                } catch (Exception e) {
                    log.warn("查询字典数据失败 dictType={}", col.getDictType(), e);
                }
            }
            return ColumnMetaVO.builder()
                .code(toCamelCase(col.getField()))
                .label(col.getTitle())
                .type(col.getDataType())
                .formType(col.getFormType())
                .isSearchable(col.getSearchable() != null && col.getSearchable() == 1)
                .isVisible(col.getShowInList() != null && col.getShowInList() == 1)
                .isSortable(col.getSortable() != null && col.getSortable() == 1)
                .isRequired(col.getRequired() != null && col.getRequired() == 1)
                .width(col.getWidth())
                .sortOrder(col.getSortOrder())
                .dictType(col.getDictType())
                .dataSource(col.getDataSource())
                .apiUrl(col.getApiUrl())
                .labelField(col.getLabelField())
                .valueField(col.getValueField())
                .componentProps(col.getComponentProps())
                .options(options)
                .colSpan(col.getColSpan())
                .isReadonly(col.getReadonly() != null && col.getReadonly() == 1)
                .isEditReadonly(col.getEditReadonly() != null && col.getEditReadonly() == 1)
                .sectionKey(col.getSectionKey())
                .sectionTitle(col.getSectionTitle())
                .sectionOrder(col.getSectionOrder())
                .sectionType(col.getSectionType())
                .sectionOpen(col.getSectionOpen())
                .i18nKey(col.getI18nKey())
                .visibleCondition(col.getVisibleCondition())
                .build();
        }).collect(java.util.stream.Collectors.toList());
    }

    /**
     * 蛇形转驼峰（统一前端 fieldCode 为驼峰，与后端 Entity 字段名一致）
     */
    private static final Pattern UNDERSCORE_PATTERN = Pattern.compile("_([a-z])");

    private String toCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }
        Matcher m = UNDERSCORE_PATTERN.matcher(snakeCase);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        // 首字母是否大写取决于原字符串
        return sb.toString();
    }
}
