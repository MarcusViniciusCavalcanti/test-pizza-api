package com.test.api.unitTest;

import com.test.api.pizza.application.dto.AddonsDTO;
import com.test.api.pizza.domain.handlers.TypeAddendHandler;
import com.test.api.pizza.domain.entity.addons.AddonsPrice;
import com.test.api.pizza.domain.entity.addons.AddonsTimeProcess;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TypeAddonsHandlerTest {


    @Test
    public void should_have_return_addend_time_process() {
        var dto = new AddonsDTO();
        dto.setName("Adicona tempo");
        dto.setValue("00:10");

        AddonsTimeProcess addons = new TypeAddendHandler().getAddendsByValueType(dto.getValue());

        assertThat(addons, notNullValue());
        assertThat(addons.getTime(), is(10));
    }

    @Test
    public void should_have_return_addend_price() {
        var dto = new AddonsDTO();
        dto.setName("Adicona $10 ao valor");
        dto.setValue("10,00");

        AddonsPrice addons = new TypeAddendHandler().getAddendsByValueType(dto.getValue());

        assertThat(addons, notNullValue());
        assertThat(addons.getPrice(), is(10.0));
    }
}