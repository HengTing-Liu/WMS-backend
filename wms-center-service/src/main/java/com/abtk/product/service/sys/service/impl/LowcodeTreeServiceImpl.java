package com.abtk.product.service.sys.service.impl;

import com.abtk.product.api.domain.response.lowcode.OccupancyCheckVO;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.mapper.DynamicMapper;
import com.abtk.product.dao.mapper.LowcodeMapper;
import com.abtk.product.dao.mapper.TableMetaMapper;
import com.abtk.product.dao.util.SqlInjectionValidator;
import com.abtk.product.service.sys.service.LowcodeTreeService;
import com.abtk.product.service.system.service.I18nService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LowcodeTreeServiceImpl implements LowcodeTreeService {

    private static final String DEFAULT_DELETE_COLUMN = "is_deleted";
    private static final String DEFAULT_PK_COLUMN = "id";
    private static final String DEFAULT_STATUS_COLUMN = "is_enabled";
    private static final String DEFAULT_PARENT_COLUMN = "parent_id";

    private static final Set<String> PAGE_PARAMS = new HashSet<>(
            Arrays.asList("pageNum", "pageSize", "orderByColumn", "isAsc")
    );

    @Autowired
    private LowcodeMapper lowcodeMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private TableMetaMapper tableMetaMapper;

    @Autowired
    private I18nService i18nService;

    private String getDeleteColumn(String tableCode) {

        return DEFAULT_DELETE_COLUMN;
    }

    private String getPkColumn(String tableCode) {
        return DEFAULT_PK_COLUMN;
    }

    private String getStatusColumn(String tableCode) {
        return DEFAULT_STATUS_COLUMN;
    }

    @Override
    public TableDataInfo listTree(String tableCode, String parentColumn, Long parentValue,
                                   Map<String, Object> params, Integer pageNum, Integer pageSize) {
        SqlInjectionValidator.validateTable(tableCode);
        SqlInjectionValidator.validateFieldFormat(parentColumn);

        Map<String, Object> searchParams = new LinkedHashMap<>();
        if (params != null) {
            params.forEach((key, value) -> {
                if (!PAGE_PARAMS.contains(key) && value != null && !"".equals(value)) {
                    searchParams.put(key, value);
                }
            });
        }

        String deleteColumn = getDeleteColumn(tableCode);
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = lowcodeMapper.selectByParent(tableCode, deleteColumn, parentColumn, parentValue);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

        TableDataInfo dataTable = new TableDataInfo();
        dataTable.setRows(list);
        dataTable.setTotal(pageInfo.getTotal());
        return dataTable;
    }

    @Override
    public List<Map<String, Object>> listTreeAll(String tableCode, String parentColumn, Map<String, Object> params) {
        SqlInjectionValidator.validateTable(tableCode);
        SqlInjectionValidator.validateFieldFormat(parentColumn);

        String deleteColumn = getDeleteColumn(tableCode);
        List<Map<String, Object>> list = lowcodeMapper.selectTreeAll(tableCode, deleteColumn, parentColumn);

        if (params != null && !params.isEmpty()) {
            Map<String, Object> searchParams = new LinkedHashMap<>();
            params.forEach((key, value) -> {
                if (!PAGE_PARAMS.contains(key) && value != null && !"".equals(value)) {
                    searchParams.put(key, value);
                }
            });
            if (!searchParams.isEmpty()) {
                final Map<String, Object> fp = searchParams;
                list = list.stream().filter(item -> {
                    for (Map.Entry<String, Object> entry : fp.entrySet()) {
                        Object val = item.get(entry.getKey());
                        if (val == null || !val.toString().contains(entry.getValue().toString())) {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
            }
        }
        return list;
    }

    @Override
    public Long countChildren(String tableCode, String parentColumn, Long parentValue) {
        SqlInjectionValidator.validateTable(tableCode);
        SqlInjectionValidator.validateFieldFormat(parentColumn);
        String deleteColumn = getDeleteColumn(tableCode);
        return lowcodeMapper.countByParent(tableCode, deleteColumn, parentColumn, parentValue);
    }

    @Override
    public List<Long> getAllDescendantIds(String tableCode, String parentColumn, Long nodeId) {
        SqlInjectionValidator.validateTable(tableCode);
        SqlInjectionValidator.validateFieldFormat(parentColumn);
        String deleteColumn = getDeleteColumn(tableCode);
        return lowcodeMapper.selectAllChildIds(tableCode, deleteColumn, parentColumn, nodeId);
    }

    @Override
    public List<Map<String, Object>> getAllDescendants(String tableCode, String parentColumn, Long nodeId) {
        SqlInjectionValidator.validateTable(tableCode);
        SqlInjectionValidator.validateFieldFormat(parentColumn);
        String deleteColumn = getDeleteColumn(tableCode);
        String pkColumn = getPkColumn(tableCode);
        List<Long> descendantIds = lowcodeMapper.selectAllChildIds(tableCode, deleteColumn, parentColumn, nodeId);
        if (descendantIds == null || descendantIds.isEmpty()) {
            return Collections.emptyList();
        }
        return lowcodeMapper.selectByIds(tableCode, pkColumn, descendantIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchToggleStatus(String tableCode, List<Long> ids, Integer enabled) {
        if (ids == null || ids.isEmpty()) return;
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        String statusColumn = getStatusColumn(tableCode);

        List<Map<String, Object>> allRecords = lowcodeMapper.selectByIds(tableCode, pkColumn, ids);
        List<String> enabledIds = new ArrayList<>();
        for (Map<String, Object> record : allRecords) {
            Object idVal = record.get(pkColumn);
            Object currentStatus = record.get(statusColumn);
            boolean shouldToggle = false;
            if (enabled != null && enabled == 1) {
                shouldToggle = currentStatus == null || "0".equals(currentStatus.toString()) || "false".equalsIgnoreCase(currentStatus.toString());
            } else {
                shouldToggle = currentStatus != null && ("1".equals(currentStatus.toString()) || "true".equalsIgnoreCase(currentStatus.toString()));
            }
            if (shouldToggle && idVal != null) {
                enabledIds.add(idVal.toString());
            }
        }
        if (enabledIds.isEmpty()) return;

        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put(statusColumn, enabled);
        updateData.put("update_time", new Date());
        updateData.put("update_by", "system");
        for (String idStr : enabledIds) {
            dynamicMapper.updateParam(tableCode, updateData, pkColumn, Long.parseLong(idStr));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(String tableCode, Long id, Integer enabled) {
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("is_enabled", enabled);
        updateData.put("update_time", new Date());
        updateData.put("update_by", "system");
        dynamicMapper.updateParam(tableCode, updateData, pkColumn, id);
    }

    @Override
    public Map<String, Object> checkDeleteOccupancy(String tableCode, Long id) {
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        String deleteColumn = getDeleteColumn(tableCode);

        Map<String, Object> record = dynamicMapper.selectByIdWithColumn(tableCode, pkColumn, id);
        String recordName = "";
        if (record != null) {
            recordName = String.valueOf(
                    record.get("name") != null ? record.get("name")
                    : record.get("warehouse_name") != null ? record.get("warehouse_name")
                    : record.get("title") != null ? record.get("title")
                    : record.get("code") != null ? record.get("code")
                    : "#" + id
            );
        }

        List<OccupancyCheckVO.OccupancyItem> occupiedItems = new ArrayList<>();
        Long childCount = lowcodeMapper.countByParent(tableCode, deleteColumn, DEFAULT_PARENT_COLUMN, id);
        if (childCount != null && childCount > 0) {
            occupiedItems.add(OccupancyCheckVO.OccupancyItem.builder()
                    .refTable(tableCode)
                    .refTableName("子节点")
                    .count(childCount)
                    .build());
        }

        boolean deletable = occupiedItems.isEmpty();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("recordName", recordName);
        result.put("deletable", deletable);
        result.put("occupied", !deletable);
        result.put("items", occupiedItems);
        return result;
    }
}
