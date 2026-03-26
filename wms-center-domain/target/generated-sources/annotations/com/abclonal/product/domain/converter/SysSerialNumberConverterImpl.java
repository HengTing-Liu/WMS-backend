package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysSerialNumberQueryRequest;
import com.abclonal.product.api.domain.request.sys.SysSerialNumberRequest;
import com.abclonal.product.api.domain.response.sys.SysSerialNumberResponse;
import com.abclonal.product.dao.entity.SysSerialNumber;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:45:22+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class SysSerialNumberConverterImpl implements SysSerialNumberConverter {

    @Override
    public SysSerialNumber requestToEntity(SysSerialNumberRequest request) {
        if ( request == null ) {
            return null;
        }

        SysSerialNumber sysSerialNumber = new SysSerialNumber();

        sysSerialNumber.setName( request.getRuleName() );
        sysSerialNumber.setUsageScope( request.getRuleCode() );
        sysSerialNumber.setDigitLength( request.getSeqLength() );
        sysSerialNumber.setCurrentValue( request.getCurrentSeq() );
        sysSerialNumber.setStartValue( request.getMaxSeq() );
        sysSerialNumber.setRemark( request.getDescription() );
        sysSerialNumber.setNumberType( dateFormatToNumberType( request.getDateFormat() ) );
        sysSerialNumber.setResetRule( resetTypeToResetRule( request.getResetType() ) );
        sysSerialNumber.setIsEnabled( statusToIsEnabled( request.getStatus() ) );
        sysSerialNumber.setCreateBy( request.getCreateBy() );
        sysSerialNumber.setCreateTime( request.getCreateTime() );
        sysSerialNumber.setUpdateBy( request.getUpdateBy() );
        sysSerialNumber.setUpdateTime( request.getUpdateTime() );
        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            sysSerialNumber.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        sysSerialNumber.setId( request.getId() );
        sysSerialNumber.setPrefix( request.getPrefix() );
        sysSerialNumber.setSuffix( request.getSuffix() );

        return sysSerialNumber;
    }

    @Override
    public SysSerialNumberResponse entityToResponse(SysSerialNumber entity) {
        if ( entity == null ) {
            return null;
        }

        SysSerialNumberResponse sysSerialNumberResponse = new SysSerialNumberResponse();

        sysSerialNumberResponse.setRuleName( entity.getName() );
        sysSerialNumberResponse.setRuleCode( entity.getUsageScope() );
        sysSerialNumberResponse.setSeqLength( entity.getDigitLength() );
        sysSerialNumberResponse.setCurrentSeq( entity.getCurrentValue() );
        sysSerialNumberResponse.setMaxSeq( entity.getStartValue() );
        sysSerialNumberResponse.setDescription( entity.getRemark() );
        sysSerialNumberResponse.setDateFormat( numberTypeToDateFormat( entity.getNumberType() ) );
        sysSerialNumberResponse.setResetType( resetRuleToResetType( entity.getResetRule() ) );
        sysSerialNumberResponse.setStatus( isEnabledToStatus( entity.getIsEnabled() ) );
        sysSerialNumberResponse.setCreateBy( entity.getCreateBy() );
        sysSerialNumberResponse.setCreateTime( entity.getCreateTime() );
        sysSerialNumberResponse.setUpdateBy( entity.getUpdateBy() );
        sysSerialNumberResponse.setUpdateTime( entity.getUpdateTime() );
        sysSerialNumberResponse.setRemark( entity.getRemark() );
        sysSerialNumberResponse.setId( entity.getId() );
        sysSerialNumberResponse.setPrefix( entity.getPrefix() );
        sysSerialNumberResponse.setSuffix( entity.getSuffix() );

        sysSerialNumberResponse.setStep( 1 );

        return sysSerialNumberResponse;
    }

    @Override
    public SysSerialNumber queryRequestToEntity(SysSerialNumberQueryRequest queryRequest) {
        if ( queryRequest == null ) {
            return null;
        }

        SysSerialNumber sysSerialNumber = new SysSerialNumber();

        sysSerialNumber.setName( queryRequest.getRuleName() );
        sysSerialNumber.setUsageScope( queryRequest.getRuleCode() );
        sysSerialNumber.setIsEnabled( statusToIsEnabled( queryRequest.getStatus() ) );
        sysSerialNumber.setResetRule( resetTypeToResetRuleForQuery( queryRequest.getResetType() ) );
        sysSerialNumber.setCreateBy( queryRequest.getCreateBy() );
        sysSerialNumber.setCreateTime( queryRequest.getCreateTime() );
        sysSerialNumber.setUpdateBy( queryRequest.getUpdateBy() );
        sysSerialNumber.setUpdateTime( queryRequest.getUpdateTime() );
        Map<String, Object> map = queryRequest.getParams();
        if ( map != null ) {
            sysSerialNumber.setParams( new LinkedHashMap<String, Object>( map ) );
        }

        return sysSerialNumber;
    }
}
