package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.sys.CustomerRequest;
import com.abtk.product.dao.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 客户档案转换器
 *
 * @author backend
 * @since 2026-03-26
 */
@Mapper
public interface CustomerConverter {

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

    /**
     * Request 转 Entity
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "createBy", ignore = true),
        @Mapping(target = "updateTime", ignore = true),
        @Mapping(target = "updateBy", ignore = true),
        @Mapping(target = "isdeleted", ignore = true)
    })
    Customer requestToEntity(CustomerRequest request);
}
