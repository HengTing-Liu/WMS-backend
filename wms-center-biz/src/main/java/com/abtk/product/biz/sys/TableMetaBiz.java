package com.abtk.product.biz.sys;

import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.api.domain.request.sys.TableMetaRequest;
import com.abtk.product.api.domain.response.sys.TableMetaResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.dao.mapper.ColumnMetaMapper;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.sys.service.TableMetaService;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表元数据业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-04-06
 */
@Slf4j
@Component
public class TableMetaBiz {

    @Autowired
    private TableMetaService tableMetaService;

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Autowired
    private I18nService i18nService;

    /** BaseEntity 固定字段（不含 createBy/createTime/updateBy/updateTime） */
    private static final List<ColumnMeta> BASE_ENTITY_COLUMNS = buildBaseEntityColumns();

    private static List<ColumnMeta> buildBaseEntityColumns() {
        List<ColumnMeta> columns = new ArrayList<>();

        String username = "system"; // 占位，创建时会被覆盖

        // createBy
        ColumnMeta createBy = new ColumnMeta();
        createBy.setField("createBy");
        createBy.setTitle("创建人");
        createBy.setColumnName("create_by");
        createBy.setDataType("string");
        createBy.setFormType("input");
        createBy.setShowInList(0);
        createBy.setShowInForm(0);
        createBy.setShowInExport(1);
        createBy.setSearchable(0);
        createBy.setSortable(0);
        createBy.setRequired(0);
        createBy.setSortOrder(900);
        createBy.setStatus(1);
        createBy.setColSpan(24);
        columns.add(createBy);

        // createTime
        ColumnMeta createTime = new ColumnMeta();
        createTime.setField("createTime");
        createTime.setTitle("创建时间");
        createTime.setColumnName("create_time");
        createTime.setDataType("datetime");
        createTime.setFormType("date");
        createTime.setShowInList(1);
        createTime.setShowInForm(0);
        createTime.setShowInExport(1);
        createTime.setSearchable(0);
        createTime.setSortable(1);
        createTime.setRequired(0);
        createTime.setSortOrder(901);
        createTime.setStatus(1);
        createTime.setColSpan(24);
        columns.add(createTime);

        // updateBy
        ColumnMeta updateBy = new ColumnMeta();
        updateBy.setField("updateBy");
        updateBy.setTitle("更新人");
        updateBy.setColumnName("update_by");
        updateBy.setDataType("string");
        updateBy.setFormType("input");
        updateBy.setShowInList(0);
        updateBy.setShowInForm(0);
        updateBy.setShowInExport(1);
        updateBy.setSearchable(0);
        updateBy.setSortable(0);
        updateBy.setRequired(0);
        updateBy.setSortOrder(902);
        updateBy.setStatus(1);
        updateBy.setColSpan(24);
        columns.add(updateBy);

        // updateTime
        ColumnMeta updateTime = new ColumnMeta();
        updateTime.setField("updateTime");
        updateTime.setTitle("更新时间");
        updateTime.setColumnName("update_time");
        updateTime.setDataType("datetime");
        updateTime.setFormType("date");
        updateTime.setShowInList(1);
        updateTime.setShowInForm(0);
        updateTime.setShowInExport(1);
        updateTime.setSearchable(0);
        updateTime.setSortable(1);
        updateTime.setRequired(0);
        updateTime.setSortOrder(903);
        updateTime.setStatus(1);
        updateTime.setColSpan(24);
        columns.add(updateTime);

        // remarks
        ColumnMeta remarks = new ColumnMeta();
        remarks.setField("remarks");
        remarks.setTitle("备注");
        remarks.setColumnName("remarks");
        remarks.setDataType("string");
        remarks.setFormType("textarea");
        remarks.setShowInList(0);
        remarks.setShowInForm(1);
        remarks.setShowInExport(1);
        remarks.setSearchable(0);
        remarks.setSortable(0);
        remarks.setRequired(0);
        remarks.setSortOrder(999);
        remarks.setStatus(1);
        remarks.setColSpan(24);
        columns.add(remarks);

        // isDeleted（逻辑删除标记）
        ColumnMeta isDeleted = new ColumnMeta();
        isDeleted.setField("isDeleted");
        isDeleted.setTitle("逻辑删除");
        isDeleted.setColumnName("is_deleted");
        isDeleted.setDataType("boolean");
        isDeleted.setFormType("switch");
        isDeleted.setShowInList(0);
        isDeleted.setShowInForm(0);
        isDeleted.setShowInExport(0);
        isDeleted.setSearchable(0);
        isDeleted.setSortable(0);
        isDeleted.setRequired(0);
        isDeleted.setSortOrder(998);
        isDeleted.setStatus(1);
        isDeleted.setColSpan(24);
        columns.add(isDeleted);

        return columns;
    }

    /**
     * 查询表元数据列表（分页）
     */
    public R<List<TableMeta>> list(TableMetaQueryRequest queryRequest) {
        TableMeta condition = new TableMeta();
        BeanUtils.copyProperties(queryRequest, condition);
        List<TableMeta> list = tableMetaService.list(condition);
        return R.ok(list);
    }

    /**
     * 查询所有表元数据（不分页）
     */
    public R<List<TableMeta>> listAll() {
        List<TableMeta> list = tableMetaService.listAll();
        return R.ok(list);
    }

    /**
     * 根据ID查询表元数据详情
     */
    public R<TableMetaResponse> queryById(Long id) {
        TableMeta entity = tableMetaService.getById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("system.meta.table.not.found"));
        }
        TableMetaResponse response = new TableMetaResponse();
        BeanUtils.copyProperties(entity, response);
        return R.ok(response);
    }

    /**
     * 根据表编码查询表元数据
     */
    public R<TableMetaResponse> queryByTableCode(String tableCode) {
        TableMeta entity = tableMetaService.getByTableCode(tableCode);
        if (entity == null) {
            return R.fail(i18nService.getMessage("system.meta.table.not.found"));
        }
        TableMetaResponse response = new TableMetaResponse();
        BeanUtils.copyProperties(entity, response);
        return R.ok(response);
    }

    /**
     * 创建表元数据，同时自动插入 BaseEntity 的固定字段
     */
    public R<Long> add(TableMetaRequest request) {
        TableMeta tableMeta = new TableMeta();
        BeanUtils.copyProperties(request, tableMeta);
        log.info("[TableMetaBiz] add() called for tableCode={}, biz 注入 BaseEntity 字段", request.getTableCode());
        Long id = tableMetaService.create(tableMeta);
        log.info("[TableMetaBiz] tableMeta created, id={}, 开始注入 BaseEntity 字段", id);

        // 插入 BaseEntity 固定字段
        String username = SecurityUtils.getUsername();
        Date now = new Date();
        for (ColumnMeta col : BASE_ENTITY_COLUMNS) {
            col.setId(null);
            col.setTableCode(request.getTableCode());
            col.setCreateBy(username);
            col.setCreateTime(now);
            log.info("[TableMetaBiz] 插入 BaseEntity 字段: field={}, title={}", col.getField(), col.getTitle());
            columnMetaMapper.insert(col);
        }

        log.info("[TableMetaBiz] add() 完成，tableCode={}, 共注入 {} 个字段", request.getTableCode(), BASE_ENTITY_COLUMNS.size());
        return R.ok(id);
    }

    /**
     * 更新表元数据
     */
    public R<Void> update(Long id, TableMetaRequest request) {
        TableMeta tableMeta = new TableMeta();
        BeanUtils.copyProperties(request, tableMeta);
        tableMetaService.update(id, tableMeta);
        return R.ok();
    }

    /**
     * 删除表元数据
     */
    public R<Void> delete(Long id) {
        tableMetaService.delete(id);
        return R.ok();
    }

    /**
     * 切换表元数据状态（启用/禁用）
     */
    public R<Void> toggleStatus(Long id, Integer status) {
        tableMetaService.toggleStatus(id, status);
        return R.ok();
    }

    /**
     * 导出表元数据列表
     */
    public List<TableMetaResponse> exportList(TableMetaQueryRequest request) {
        TableMeta condition = new TableMeta();
        BeanUtils.copyProperties(request, condition);
        List<TableMeta> list = tableMetaService.list(condition);
        return list.stream()
                .map(entity -> {
                    TableMetaResponse response = new TableMetaResponse();
                    BeanUtils.copyProperties(entity, response);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
