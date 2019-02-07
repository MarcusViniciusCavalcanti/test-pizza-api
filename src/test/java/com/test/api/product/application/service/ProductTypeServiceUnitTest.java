package com.test.api.product.application.service;

import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.repository.ProductTypeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.After;
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
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ProductTypeServiceUnitTest {
    private static final int ID_SMALL = 1;
    private static final int ID_MIDDLE = 2;
    private static final int ID_BIGGER = 3;

    private static final String NAME_SMALL = "pequena";
    private static final String NAME_MIDDLE = "média";
    private static final String NAME_BIGGER = "grande";

    private static final int ID_NOT_FOUND = 10000;

    private ProductType small = new ProductType();
    private ProductType middle = new ProductType();
    private ProductType bigger = new ProductType();

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

    @Before
    public void setUp() throws Exception {
        small.setId(ID_SMALL);
        small.setName(NAME_SMALL);
        small.setTimeProcess(15);

        middle.setId(ID_MIDDLE);
        middle.setName(NAME_MIDDLE);
        middle.setTimeProcess(20);

        bigger.setId(ID_BIGGER);
        bigger.setName(NAME_BIGGER);
        bigger.setTimeProcess(25);

        Mockito.when(productTypeRepository.findById(ID_SMALL)).thenReturn(Optional.of(small));
        Mockito.when(productTypeRepository.findById(ID_MIDDLE)).thenReturn(Optional.of(middle));
        Mockito.when(productTypeRepository.findById(ID_BIGGER)).thenReturn(Optional.of(bigger));

        Mockito.when(productTypeRepository.findAll()).thenReturn(Arrays.asList(small, middle, bigger));

        Mockito.when(productTypeRepository.findById(ID_NOT_FOUND)).thenThrow(new EntityNotFoundException());
    }

    @Test
    public void should_return_successfully_by_id() {
        var small = productTypeService.getById(ID_SMALL);
        var bigger = productTypeService.getById(ID_BIGGER);

        assertThat(small.getName(), CoreMatchers.is(NAME_SMALL));
        assertThat(bigger.getName(), CoreMatchers.is(NAME_BIGGER));
    }

    @Test
    public void should_return_successfully_all_objects() {
        var entities = productTypeService.getAll();

        assertThat(entities.size(), CoreMatchers.is(3));
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_return_not_found_exception() {
        var other = productTypeService.getById(ID_NOT_FOUND);
    }
}