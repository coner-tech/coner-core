package org.coner.hibernate.dao;

import java.util.List;

import org.coner.hibernate.entity.HibernateEntity;

public interface HibernateEntityDao<HE extends HibernateEntity> {
    void create(HE entity);

    List<HE> findAll();

    HE findById(String id);
}
