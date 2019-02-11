package com.test.api.pizza.application.validations;

import java.util.regex.Pattern;

public interface ConstraintAddons {

    static boolean checkTime(String value) {
       return Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]").asPredicate().test(value);
    }

    static boolean checkMonetaryValue(String value) {
        return Pattern.compile("[\\t ]*((\\d{1,3}\\.?)+(,\\d{2})?)").asPredicate().test(value);
    }
}