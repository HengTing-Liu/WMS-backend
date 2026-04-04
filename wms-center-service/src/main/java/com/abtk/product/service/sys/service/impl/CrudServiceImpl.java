package com.abtk.product.service.sys.service.impl;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.mapper.DynamicMapper;
import com.abtk.product.dao.mapper.TableMetaMapper;
import com.abtk.product.dao.util.SqlInjectionValidator;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.service.permission.util.CrudPermissionUtil;
import com.abtk.product.service.sys.service.CrudService;
import com.abtk.product.service.system.service.I18nService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用 CRUD 服务实现
 */
@Service
public class CrudServiceImpl implements CrudService {

    /** sys_user 表标识 */
    private static final String SYS_USER_TABLE = "sys_user";

    /** sys_user 主键列名 */
    private static final String SYS_USER_PK = "user_id";

    /** 默认逻辑删除列名 */
    private static final String DEFAULT_DELETE_COLUMN = "isdeleted";

    /** 分页参数名（需要从查询条件中排除） */
    private static final Set<String> PAGE_PARAMS = new HashSet<>(
            Arrays.asList("pageNum", "pageSize", "orderByColumn", "isAsc")
    );
    /** 字段名白名单正则：仅允许字母、数字、下划线 */
    private static final java.util.regex.Pattern SAFE_FIELD_PATTERN =
            java.util.regex.Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private TableMetaMapper tableMetaMapper;

    @Autowired
    private I18nService i18nService;

    /**
     * 从 sys_table_meta 获取表的逻辑删除列名
     * @param tableCode 表标识
     * @return 删除列名，未配置则返回默认值 isdeleted
     */
    private String getDeleteColumn(String tableCode) {
        TableMeta meta = tableMetaMapper.selectByTableCode(tableCode);
        if (meta != null && StringUtils.isNotEmpty(meta.getIsDeletedColumn())) {
            return meta.getIsDeletedColumn();
        }
        return DEFAULT_DELETE_COLUMN;
    }

    /**
     * 获取表的主键列名
     * @param tableCode 表标识
     * @return 主键列名
     */
    private String getPkColumn(String tableCode) {
        if (SYS_USER_TABLE.equals(tableCode)) {
            return SYS_USER_PK;
        }
        return "id";
    }

    @Override
    public TableDataInfo list(String tableCode, Map<String, Object> params, Integer pageNum, Integer pageSize) {
        SqlInjectionValidator.validateTable(tableCode);
        // 过滤掉分页参数，避免被当作查询条件
        Map<String, Object> searchParams = new HashMap<>();
        if (params != null) {
            Map<String, Object> finalSearchParams = searchParams;
            params.forEach((key, value) -> {
                if (!PAGE_PARAMS.contains(key) && value != null && !"".equals(value)) {
                    finalSearchParams.put(key, value);
                }
            });
        }
        searchParams = CrudPermissionUtil.injectDataScope(searchParams);
        PageHelper.startPage(pageNum, pageSize);
        String deleteColumn = getDeleteColumn(tableCode);
        List<Map<String, Object>> list = dynamicMapper.selectList(tableCode, searchParams, deleteColumn);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        TableDataInfo dataTable = new TableDataInfo();
        dataTable.setRows(list);
        dataTable.setTotal(pageInfo.getTotal());
        return dataTable;
    }

    @Override
    public List<Map<String, Object>> listAll(String tableCode, Map<String, Object> params) {
        SqlInjectionValidator.validateTable(tableCode);
        // 过滤掉空值参数
        Map<String, Object> searchParams = new HashMap<>();
        if (params != null) {
            Map<String, Object> finalSearchParams = searchParams;
            params.forEach((key, value) -> {
                if (!PAGE_PARAMS.contains(key) && value != null && !"".equals(value)) {
                    finalSearchParams.put(key, value);
                }
            });
        }
        searchParams = CrudPermissionUtil.injectDataScope(searchParams);
        String deleteColumn = getDeleteColumn(tableCode);
        return dynamicMapper.selectAll(tableCode, searchParams, deleteColumn);
    }

    @Override
    public Map<String, Object> getById(String tableCode, Long id) {
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        Map<String, Object> result = dynamicMapper.selectByIdWithColumn(tableCode, pkColumn, id);
        if (result == null) {
            throw new ServiceException(i18nService.getMessage("crud.entity.not.found", tableCode));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(String tableCode, Map<String, Object> data) {
        SqlInjectionValidator.validateTable(tableCode);
        data.put("create_time", new Date());
        data.put("create_by", "system");
        if (SYS_USER_TABLE.equals(tableCode) && data.containsKey("user_id")) {
            data.put("id", data.get("user_id"));
        }
        // 校验所有字段名
        for (String key : data.keySet()) {
            if (!SAFE_FIELD_PATTERN.matcher(key).matches()) {
                throw new ServiceException("非法的字段名: " + key);
            }
        }
        // 分离列名和值，使用参数化查询
        List<String> columns = new ArrayList<>(data.keySet());
        List<Object> values = new ArrayList<>();
        for (String col : columns) {
            values.add(data.get(col));
        }
        dynamicMapper.insertParam(tableCode, columns, values);
        Map<String, Object> lastId = dynamicMapper.selectLastInsertId();
        if (lastId != null && lastId.get("id") != null) {
            return ((Number) lastId.get("id")).longValue();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String tableCode, Long id, Map<String, Object> data) {
        SqlInjectionValidator.validateTable(tableCode);
        data.put("update_time", new Date());
        data.put("update_by", "system");
        // sys_user 表没有 id 列，需要移除 data 中的 id 键避免 "Unknown column 'id'" 错误
        if (SYS_USER_TABLE.equals(tableCode)) {
            data.remove("id");
        }
        // 校验所有字段名
        for (String key : data.keySet()) {
            if (!SAFE_FIELD_PATTERN.matcher(key).matches()) {
                throw new ServiceException("非法的字段名: " + key);
            }
        }
        // 获取主键列名
        String pkColumn = getPkColumn(tableCode);
        // 使用参数化更新
        dynamicMapper.updateParam(tableCode, data, pkColumn, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String tableCode, Long id) {
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        String deleteColumn = getDeleteColumn(tableCode);
        dynamicMapper.logicDelete(tableCode, pkColumn, id, deleteColumn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String tableCode, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        SqlInjectionValidator.validateTable(tableCode);
        String pkColumn = getPkColumn(tableCode);
        String deleteColumn = getDeleteColumn(tableCode);
        dynamicMapper.batchLogicDelete(tableCode, pkColumn, ids, deleteColumn);
    }

    @Override
    public boolean checkUnique(String tableCode, String field, String value, Long excludeId) {
        SqlInjectionValidator.validateTable(tableCode);
        SqlInjectionValidator.validateField(field);
        Long count = dynamicMapper.checkUnique(tableCode, field, value, excludeId);
        return count == 0;
    }
}
