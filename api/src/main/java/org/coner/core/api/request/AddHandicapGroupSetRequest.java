package org.coner.core.api.request;

import java.util.Set;

import org.hibernate.validator.constraints.NotBlank;

public class AddHandicapGroupSetRequest {

    @NotBlank
    private String name;
    private Set<HandicapGroup> handicapGroups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HandicapGroup> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(Set<HandicapGroup> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }

    public static class HandicapGroup {
        @NotBlank
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HandicapGroup that = (HandicapGroup) o;

            return !(id != null ? !id.equals(that.id) : that.id != null);

        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddHandicapGroupSetRequest that = (AddHandicapGroupSetRequest) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(handicapGroups != null ? !handicapGroups.equals(that.handicapGroups) : that.handicapGroups != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (handicapGroups != null ? handicapGroups.hashCode() : 0);
        return result;
    }
}
