package org.coner.core.hibernate.dao;

import java.util.List;

public interface HibernateEntityDao<HE> {
    void create(HE entity);

    List<HE> findAll();

    HE findById(String id);

    void update(HE entity);
}
