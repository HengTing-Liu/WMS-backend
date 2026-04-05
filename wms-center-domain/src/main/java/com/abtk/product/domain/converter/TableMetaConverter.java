package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.sys.TableMetaRequest;
import com.abtk.product.api.domain.response.sys.TableMetaResponse;
import com.abtk.product.dao.entity.TableMeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 表元数据转换器
 *
 * @author backend
 * @since 2026-04-06
 */
@Mapper
public interface TableMetaConverter {

    TableMetaConverter INSTANCE = Mappers.getMapper(TableMetaConverter.class);

    /**
     * Request 转 Entity
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "createBy", ignore = true),
        @Mapping(target = "updateTime", ignore = true),
        @Mapping(target = "updateBy", ignore = true),
        @Mapping(target = "searchValue", ignore = true),
        @Mapping(target = "params", ignore = true),
        @Mapping(target = "columns", ignore = true),
        @Mapping(target = "operations", ignore = true)
    })
    TableMeta requestToEntity(TableMetaRequest request);

    /**
     * Entity 转 Response
     */
    default TableMetaResponse entityToResponse(TableMeta entity) {
        if (entity == null) {
            return null;
        }
        TableMetaResponse response = new TableMetaResponse();
        response.setId(entity.getId());
        response.setTableCode(entity.getTableCode());
        response.setTableName(entity.getTableName());
        response.setModule(entity.getModule());
        response.setEntityClass(entity.getEntityClass());
        response.setServiceClass(entity.getServiceClass());
        response.setPermissionCode(entity.getPermissionCode());
        response.setPageSize(entity.getPageSize());
        response.setIsTree(entity.getIsTree());
        response.setStatus(entity.getStatus());
        response.setRemark(entity.getRemark());
        response.setIsDeletedColumn(entity.getIsDeletedColumn());
        response.setCreateBy(entity.getCreateBy());
        response.setUpdateBy(entity.getUpdateBy());

        // Date 转 LocalDateTime
        if (entity.getCreateTime() != null) {
            response.setCreateTime(LocalDateTime.ofInstant(entity.getCreateTime().toInstant(), ZoneId.systemDefault()));
        }
        if (entity.getUpdateTime() != null) {
            response.setUpdateTime(LocalDateTime.ofInstant(entity.getUpdateTime().toInstant(), ZoneId.systemDefault()));
        }

        return response;
    }
}
