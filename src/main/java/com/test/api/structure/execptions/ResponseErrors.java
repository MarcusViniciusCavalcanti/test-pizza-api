package com.test.api.structure.execptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseErrors {
    private String title;
    private int statusCode;
    private long timestamp;
    private String exception;
    private Object error;

    private ResponseErrors() { }

    public static final class Builder {
        private ResponseErrors responseErrors;

        private Builder() {
            responseErrors = new ResponseErrors();
        }

        public static Builder newResponse() {
            return new Builder();
        }

        public Builder withTitle(String title) {
            responseErrors.setTitle(title);
            return this;
        }

        public Builder withStatusCode(int statusCode) {
            responseErrors.setStatusCode(statusCode);
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            responseErrors.setTimestamp(timestamp);
            return this;
        }

        public Builder withException(Class<?> cls) {
            responseErrors.setException(cls.getSimpleName());
            return this;
        }

        public Builder withErrors(String message) {
            responseErrors.setError(message);
            return this;
        }

        public Builder withErrors(Object errors) {
            responseErrors.setError(errors);
            return this;
        }

        public ResponseErrors build() {
            return responseErrors;
        }
    }
}
