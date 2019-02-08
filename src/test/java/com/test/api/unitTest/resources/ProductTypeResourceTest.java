package com.test.api.unitTest.resources;

import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.resource.ProductTypeResource;
import com.test.api.utils.FactoryObjectsToTest;
import com.test.api.utils.ProductTypeFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ProductTypeResourceTest {

    private FactoryObjectsToTest<ProductType> productTypeFactory = new ProductTypeFactory();

    @Test
    public void should_return_resource_with_attributes_exact() {
        var productType = productTypeFactory.createOneObject();
        var resource = new ProductTypeResource();
        resource.copyAttributes(productType);

        assertThat(productType.getId(), CoreMatchers.is(resource.getProductTypeId()));
        assertThat(productType.getName(), CoreMatchers.is(resource.getName()));
        assertThat(productType.getTimeProcess(), CoreMatchers.is(resource.getTimeProcessInMinute()));
    }
}