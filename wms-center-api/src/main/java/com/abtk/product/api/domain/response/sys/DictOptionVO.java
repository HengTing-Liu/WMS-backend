package com.abtk.product.api.domain.response.sys;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class DictOptionVO {

    private String value;

    private String label;
}
