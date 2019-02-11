package com.test.api.unitTest.services;

import com.test.api.ApiApplication;
import com.test.api.pizza.application.dto.AddonsDTO;
import com.test.api.pizza.application.service.AddonsService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/beforeTestRun.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:scripts/afterTestRun.sql", config = @SqlConfig(transactionMode = ISOLATED))
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class AddonsServiceTest {

    @Autowired
    private AddonsService addonsService;

    @Test
    public void should_have_return_addonsTimeProcess() {
        var timeProcess = addonsService.getById(1);

        assertThat(timeProcess, CoreMatchers.notNullValue());
        assertThat(timeProcess.getName(), CoreMatchers.is("add five minute in process"));
    }

    @Test
    public void should_have_return_addonsPrice() {
        var timeProcess = addonsService.getById(2);

        assertThat(timeProcess, CoreMatchers.notNullValue());
        assertThat(timeProcess.getName(), CoreMatchers.is("add five in price"));
    }

    @Test
    public void should_have_save_correct_addons_by_addonsDTO() {
        var timeDTO = new AddonsDTO();
        timeDTO.setName("Adiciona 11 minuto ao tempo de processo");
    }
}