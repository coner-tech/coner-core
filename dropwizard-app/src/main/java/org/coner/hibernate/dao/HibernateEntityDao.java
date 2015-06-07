package org.coner.hibernate.dao;

import org.coner.hibernate.entity.HibernateEntity;

import java.util.List;

public interface HibernateEntityDao<HE extends HibernateEntity> {
    void create(HE entity);

    List<HE> findAll();

    HE findById(String id);
}
