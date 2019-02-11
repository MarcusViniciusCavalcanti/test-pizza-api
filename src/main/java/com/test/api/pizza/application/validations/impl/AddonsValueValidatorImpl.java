package com.test.api.pizza.application.validations.impl;

import com.test.api.pizza.application.validations.AddonsValueValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.test.api.pizza.application.validations.ConstraintAddons.checkMonetaryValue;
import static com.test.api.pizza.application.validations.ConstraintAddons.checkTime;

public class AddonsValueValidatorImpl implements ConstraintValidator<AddonsValueValidator, String> {

    @Override
    public void initialize(AddonsValueValidator constraintAnnotation) {
        // required
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && (checkTime(value) || checkMonetaryValue(value));
    }
}
