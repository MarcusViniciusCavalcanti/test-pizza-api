package com.test.api.product.domain.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.api.product.domain.entity.ProductType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductTypeResource extends ResourceSupport {
    private Integer productTypeId;
    private String name;
    private Integer timeProcessInMinute;

    public void copyAttributes(ProductType productType) {
        this.productTypeId = productType.getId();
        this.name = productType.getName();
        this.timeProcessInMinute = productType.getTimeProcess();
    }
}
