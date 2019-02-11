package com.test.api.utils;

import org.assertj.core.util.Lists;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;

import java.util.List;

public class FieldDescriptToErrors {

    public static List<FieldDescriptor> getDefaultFieldDescript() {
        return Lists.list(
                PayloadDocumentation.fieldWithPath("title").description("O erro ocorrido, e.g `Bad Request`"),
                PayloadDocumentation.fieldWithPath("statusCode").description("Code de Status HTTP. e.g `400`"),
                PayloadDocumentation.fieldWithPath("timestamp").description("O momento do em millisegundos"),
                PayloadDocumentation.fieldWithPath("exception").description("O tipo da exception lançada"),
                PayloadDocumentation.fieldWithPath("error").description("Objeto contendo o error")
        );
    }
}
