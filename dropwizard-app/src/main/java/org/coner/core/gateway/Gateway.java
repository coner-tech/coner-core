package org.coner.core.gateway;

import java.util.List;

public interface Gateway<DE, HE> {
    void create(DE createEntity);

    List<DE> getAll();

    DE findById(String id);
}
