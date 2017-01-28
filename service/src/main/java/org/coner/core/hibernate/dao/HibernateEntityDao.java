package org.coner.core.hibernate.dao;

import java.util.List;

import org.coner.core.hibernate.entity.HibernateEntity;

public interface HibernateEntityDao<HE extends HibernateEntity> {
    void create(HE entity);

    List<HE> findAll();

    HE findById(String id);
}
