package org.coner.core.gateway;

import java.util.List;

public interface Gateway<DE, AP> {

    DE add(AP addPayload);

    List<DE> getAll();

    DE findById(String id);

    DE save(String id, DE entity);
}
