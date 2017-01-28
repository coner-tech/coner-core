package org.coner.core.api.response;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ErrorsResponse {

    public ErrorsResponse() {
        // no-op
    }

    public ErrorsResponse(String... errors) {
        this.errors = ImmutableList.copyOf(errors);
    }

    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = ImmutableList.copyOf(errors);
    }
}
