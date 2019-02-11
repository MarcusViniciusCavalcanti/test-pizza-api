package com.test.api.pizza.application.validations;

import com.test.api.pizza.application.validations.impl.AddonsValueValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AddonsValueValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface AddonsValueValidator {
    String message() default "Value not correspond expected, please using 00:00 to time or 0,00 to price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
