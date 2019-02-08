package com.test.api.product.application.service;

import com.test.api.product.application.dto.ProductTypeDTO;
import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.repository.ProductTypeRepository;
import com.test.api.utils.FactoryObjectsToTest;
import com.test.api.utils.ProductTypeFactory;
import org.assertj.core.util.Maps;
import org.hamcrest.CoreMatchers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.test.api.utils.ProductTypeFactory.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class ProductTypeServiceUnitTest {

    @Configuration
    static class config {
        @Autowired
        ProductTypeRepository productTypeRepository;
        @Bean
        public ProductTypeService getProductTypeService() {
            return new ProductTypeService(productTypeRepository);
        }
    }

    @Autowired
    private ProductTypeService productTypeService;

    @MockBean
    private ProductTypeRepository productTypeRepository;

    private FactoryObjectsToTest<ProductType> productTypeFactory = new ProductTypeFactory();

    @Test
    public void should_return_successfully_by_id() {
        var small = productTypeFactory.createOneObject();
        Mockito.when(productTypeRepository.findById(ID_SMALL)).thenReturn(Optional.of(small));

        var result = productTypeService.getById(ID_SMALL);
        assertThat(result.getName(), CoreMatchers.is(small.getName()));
    }

    @Test
    public void should_return_successfully_all_objects() {
        var list = productTypeFactory.createListObjects();
        Mockito.when(productTypeRepository.findAll()).thenReturn(list);

        var result = productTypeService.getAll();

        assertThat(result.size(), CoreMatchers.is(list.size()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_return_not_found_exception() {
        productTypeService.getById(ID_NOT_FOUND);
    }

    @Test
    public void should_save_successfully() {
        var args = new HashMap<String, Object>();
        args.put("id", ID_SAVE);
        args.put("name", NAME_SAVE);
        args.put("time", MINUTE_15);

        var small = productTypeFactory.createObjectBy(args);
        Mockito.when(productTypeRepository.save(Mockito.any(ProductType.class))).thenReturn(small);

        var dto = new ProductTypeDTO();
        dto.setName(NAME_SAVE);
        dto.setTimeProcess(0);

        var result = productTypeService.save(dto);

        verify(productTypeRepository, times(1)).save(Mockito.any(ProductType.class));

        assertThat(result.getName(), CoreMatchers.is(small.getName()));
        assertThat(result.getId(), CoreMatchers.notNullValue());
    }
}