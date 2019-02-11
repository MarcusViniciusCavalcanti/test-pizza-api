package com.test.api.unitTest.validations;

import com.test.api.pizza.application.dto.AddonsDTO;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class AddonsValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_have_return_error_when_value_is_missing() {
        var dto = new AddonsDTO();
        dto.setName("com valor faltando");

        Set<ConstraintViolation<AddonsDTO>> violations = validator.validate(dto);

        assertThat(violations, CoreMatchers.notNullValue());
        assertThat(violations, hasSize(1));
    }

    @Test
    public void should_have_return_error_when_value_is_invalid() {
        var dto = new AddonsDTO();
        dto.setName("um nome do adicional");
        dto.setValue("este valor e inválido");

        Set<ConstraintViolation<AddonsDTO>> violations = validator.validate(dto);

        assertThat(violations, CoreMatchers.notNullValue());
        assertThat(violations, hasSize(1));
    }

    @Test
    public void should_have_success_when_value_is_time() {
        var dto = new AddonsDTO();
        dto.setName("com minuto");
        dto.setValue("00:10");

        Set<ConstraintViolation<AddonsDTO>> violations = validator.validate(dto);

        assertThat(violations, empty());
    }

    @Test
    public void should_have_success_when_value_is_monetary_value() {
        var dto = new AddonsDTO();
        dto.setName("com real");
        dto.setValue("1,00");

        Set<ConstraintViolation<AddonsDTO>> violations = validator.validate(dto);

        assertThat(violations, empty());
    }
}
