package com.test.api.structure.execptions;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

public class InvalidParamsException  extends IllegalArgumentException {
    private String title;
    private BindingResult result;

    public InvalidParamsException(String title, BindingResult result) {
        super();
        this.title = title;
        this.result = result;
    }

    public List<FieldError> errors() {
        if (result.hasErrors()) {
            return result.getFieldErrors();
        }
        return Collections.emptyList();
    }

    public String getTitle() {
        return this.title;
    }
}