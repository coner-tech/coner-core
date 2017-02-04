package org.coner.core.api.response;

import java.util.List;

import org.coner.core.api.entity.ApiEntity;

public abstract class ApiEntityListingResponse<T extends ApiEntity> {

    private List<T> entities;

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

}
