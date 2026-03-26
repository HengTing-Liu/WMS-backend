package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.CustomerRequest;
import com.abclonal.product.dao.entity.Customer;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:45:22+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class CustomerConverterImpl implements CustomerConverter {

    @Override
    public Customer requestToEntity(CustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer customer = new Customer();

        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            customer.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        customer.setCustomerCode( request.getCustomerCode() );
        customer.setCustomerName( request.getCustomerName() );
        customer.setContactPerson( request.getContactPerson() );
        customer.setContactPhone( request.getContactPhone() );
        customer.setMobile( request.getMobile() );
        customer.setEmail( request.getEmail() );
        customer.setProvince( request.getProvince() );
        customer.setCity( request.getCity() );
        customer.setDistrict( request.getDistrict() );
        customer.setAddress( request.getAddress() );
        customer.setIsEnabled( request.getIsEnabled() );
        customer.setRemark( request.getRemark() );

        return customer;
    }
}
