package com.test.api.utils;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.RequestDispatcher;
import java.util.Objects;

public class HandlerErrorTest {
    public static ResultHandler invoke(MockMvc mockMvc) {
        return result -> {
            if (result.getResolvedException() != null) {
                byte[] response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/error")
                                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, result.getResponse()
                                        .getStatus()
                                )
                                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI,
                                        Objects.requireNonNull(result.getRequest().getRequestURI())
                                )
                                .requestAttr(RequestDispatcher.ERROR_EXCEPTION, result.getResolvedException())
                                .requestAttr(RequestDispatcher.ERROR_MESSAGE, String.valueOf(result.getResponse()
                                        .getErrorMessage())
                                )
                )
                        .andReturn()
                        .getResponse()
                        .getContentAsByteArray();

                result.getResponse()
                        .getOutputStream()
                        .write(response);
            }
        };
    }
}
