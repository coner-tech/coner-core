package org.coner.core.api.request;

import java.util.Objects;
import java.util.Set;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddHandicapGroupSetRequest that = (AddHandicapGroupSetRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(handicapGroupIds, that.handicapGroupIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, handicapGroupIds);
    }
}
