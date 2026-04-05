package com.abtk.product.biz.sys;

import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.api.domain.request.sys.TableMetaRequest;
import com.abtk.product.api.domain.response.sys.TableMetaResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.TableMeta;
import com.abtk.product.domain.converter.TableMetaConverter;
import com.abtk.product.service.sys.service.TableMetaService;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private I18nService i18nService;

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
        TableMetaResponse response = TableMetaConverter.INSTANCE.entityToResponse(entity);
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
        TableMetaResponse response = TableMetaConverter.INSTANCE.entityToResponse(entity);
        return R.ok(response);
    }

    /**
     * 创建表元数据
     */
    public R<Long> add(TableMetaRequest request) {
        // 使用Converter转换Request到Entity
        TableMeta tableMeta = TableMetaConverter.INSTANCE.requestToEntity(request);

        Long id = tableMetaService.create(tableMeta);
        return R.ok(id);
    }

    /**
     * 更新表元数据
     */
    public R<Void> update(Long id, TableMetaRequest request) {
        // 使用Converter转换Request到Entity
        TableMeta tableMeta = TableMetaConverter.INSTANCE.requestToEntity(request);

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
                .map(TableMetaConverter.INSTANCE::entityToResponse)
                .collect(Collectors.toList());
    }
}
