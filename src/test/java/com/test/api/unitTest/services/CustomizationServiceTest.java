package com.test.api.unitTest.services;

import com.test.api.ApiApplication;
import com.test.api.pizza.application.service.CustomizationsService;
import com.test.api.pizza.domain.entity.Customization;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/beforeTestRun.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:scripts/afterTestRun.sql", config = @SqlConfig(transactionMode = ISOLATED))
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class CustomizationServiceTest {

    @Autowired
    private CustomizationsService customizationsService;

    @Test
    public void should_have_customizations_when_multiples_ids() {
        var ids = List.of(1,2,3);

        List<Customization> result = customizationsService.getByIds(ids);

        assertThat(result, CoreMatchers.notNullValue());
        assertThat(result, Matchers.hasSize(3));
    }
}