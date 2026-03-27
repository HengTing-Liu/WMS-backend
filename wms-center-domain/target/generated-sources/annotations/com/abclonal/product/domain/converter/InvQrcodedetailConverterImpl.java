package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.inv.InvQrcodedetailRequest;
import com.abclonal.product.api.domain.response.inv.InvQrcodedetailResponse;
import com.abclonal.product.dao.entity.InvQrcodedetail;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-27T10:16:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class InvQrcodedetailConverterImpl implements InvQrcodedetailConverter {

    @Override
    public InvQrcodedetail requestToInvQrcodedetail(InvQrcodedetailRequest vo) {
        if ( vo == null ) {
            return null;
        }

        InvQrcodedetail invQrcodedetail = new InvQrcodedetail();

        invQrcodedetail.setCreateBy( vo.getCreateBy() );
        invQrcodedetail.setCreateTime( vo.getCreateTime() );
        invQrcodedetail.setUpdateBy( vo.getUpdateBy() );
        invQrcodedetail.setUpdateTime( vo.getUpdateTime() );
        Map<String, Object> map = vo.getParams();
        if ( map != null ) {
            invQrcodedetail.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        invQrcodedetail.setId( vo.getId() );
        invQrcodedetail.setIsDeleted( vo.getIsDeleted() );
        invQrcodedetail.setQrcode( vo.getQrcode() );

        return invQrcodedetail;
    }

    @Override
    public InvQrcodedetailResponse InvQrcodedetailToResponse(InvQrcodedetail invQrcodedetail) {
        if ( invQrcodedetail == null ) {
            return null;
        }

        InvQrcodedetailResponse invQrcodedetailResponse = new InvQrcodedetailResponse();

        invQrcodedetailResponse.setCreateBy( invQrcodedetail.getCreateBy() );
        invQrcodedetailResponse.setCreateTime( invQrcodedetail.getCreateTime() );
        invQrcodedetailResponse.setUpdateBy( invQrcodedetail.getUpdateBy() );
        invQrcodedetailResponse.setUpdateTime( invQrcodedetail.getUpdateTime() );
        invQrcodedetailResponse.setRemark( invQrcodedetail.getRemark() );
        invQrcodedetailResponse.setId( invQrcodedetail.getId() );
        invQrcodedetailResponse.setIsDeleted( invQrcodedetail.getIsDeleted() );
        invQrcodedetailResponse.setQrcode( invQrcodedetail.getQrcode() );

        return invQrcodedetailResponse;
    }
}
