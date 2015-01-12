package org.coner.api.response;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * API response object when one or more errors occurred.
 */
public class ErrorsResponse {

    private ImmutableList<String> errors;

    public ImmutableList<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = ImmutableList.copyOf(errors);
    }
}
