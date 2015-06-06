package org.coner.hibernate.dao;

import org.coner.hibernate.entity.HandicapGroup;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class HandicapGroupDao extends AbstractDAO<HandicapGroup> {

    public HandicapGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void create(HandicapGroup handicapGroup) {
        persist(handicapGroup);
    }

    public List<HandicapGroup> findAll() {
        return list(namedQuery(HandicapGroup.QUERY_FIND_ALL));
    }

    public HandicapGroup findById(String id) {
        return get(id);
    }
}
