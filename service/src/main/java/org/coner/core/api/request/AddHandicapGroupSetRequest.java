package org.coner.core.api.request;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class AddHandicapGroupSetRequest {

    @NotBlank
    private String name;
    private Set<String> handicapGroupIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getHandicapGroupIds() {
        return handicapGroupIds;
    }

    public void setHandicapGroupIds(Set<String> handicapGroupIds) {
        this.handicapGroupIds = handicapGroupIds;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
