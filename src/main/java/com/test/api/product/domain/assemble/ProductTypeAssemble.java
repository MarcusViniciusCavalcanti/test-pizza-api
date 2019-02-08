package com.test.api.product.domain.assemble;

import com.test.api.product.application.endpoint.ProductTypeEndpoint;
import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.resource.ProductTypeResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class ProductTypeAssemble extends ResourceAssemblerSupport<ProductType, ProductTypeResource> {

    public ProductTypeAssemble() {
        super(ProductTypeEndpoint.class, ProductTypeResource.class);
    }

    @Override
    public ProductTypeResource toResource(ProductType productType) {
        var resource = createResourceWithId(productType.getId(), productType);
        resource.copyAttributes(productType);
        return resource;
    }
}
