package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.inv.InvQrcodedetailRequest;
import com.abtk.product.api.domain.response.inv.InvQrcodedetailResponse;
import com.abtk.product.dao.entity.InvQrcodedetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 15:47
 *
 */
@Mapper
public interface InvQrcodedetailConverter {
    InvQrcodedetailConverter INSTANCE = Mappers.getMapper(InvQrcodedetailConverter.class);

    InvQrcodedetail requestToInvQrcodedetail(InvQrcodedetailRequest vo);

    InvQrcodedetailResponse InvQrcodedetailToResponse(InvQrcodedetail invQrcodedetail);

}

