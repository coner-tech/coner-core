package org.coner.core.gateway;

import java.util.List;

public interface Gateway<DE, AP> {
//    void create(DE createEntity);

    DE add(AP addPayload);

    List<DE> getAll();

    DE findById(String id);
}
