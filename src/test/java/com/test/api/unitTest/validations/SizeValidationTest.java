package com.test.api.unitTest.validations;

import com.test.api.order.application.dtos.OrderDTO;
import com.test.api.order.application.validations.EnumValidator;
import com.test.api.order.application.validations.impl.EnumValidatorImpl;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SizeValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_have_error_when_size_not_exist() {
        var dto = new OrderDTO();
        dto.setPizzaSize("not_exist");
        dto.setFlavorId(1);

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(dto);

        assertThat(violations, CoreMatchers.notNullValue());
        assertThat(violations, hasSize(1));
    }

    @Test
    public void should_have_error_when_size_is_missing() {
        var dto = new OrderDTO();

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(dto);

        assertThat(violations, CoreMatchers.notNullValue());
        assertThat(violations, hasSize(3));
    }

    @Test
    public void should_have_success_when_selection_size_is_exist() {
        var small = new OrderDTO();
        var middle = new OrderDTO();
        var bigger = new OrderDTO();

        small.setPizzaSize("small");
        small.setFlavorId(1);
        middle.setPizzaSize("middle");
        middle.setFlavorId(1);
        bigger.setPizzaSize("bigger");
        bigger.setFlavorId(1);

        Set<ConstraintViolation<OrderDTO>> violationsSmall = validator.validate(small);
        Set<ConstraintViolation<OrderDTO>> violationsMiddle = validator.validate(middle);
        Set<ConstraintViolation<OrderDTO>> violationsBigger = validator.validate(bigger);

        assertThat(violationsSmall, empty());
        assertThat(violationsMiddle, empty());
        assertThat(violationsBigger, empty());
    }

}
