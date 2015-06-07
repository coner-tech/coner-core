package org.coner.core.domain.service;

import org.coner.core.gateway.Gateway;

import java.util.List;

public abstract class AbstractDomainService<DE, G extends Gateway<DE, ?>> {

    private final G gateway;

    protected AbstractDomainService(G gateway) {
        this.gateway = gateway;
    }

    public void add(DE addEntity) {
        gateway.create(addEntity);
    }

    public DE getById(String id) {
        return gateway.findById(id);
    }

    public List<DE> getAll() {
        return gateway.getAll();
    }

    protected G getGateway() {
        return gateway;
    }

}
