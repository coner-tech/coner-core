package org.coner.hibernate.dao;

import org.coner.hibernate.entity.CompetitionGroup;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class CompetitionGroupDao extends AbstractDAO<CompetitionGroup> {

    public CompetitionGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void createOrUpdate(CompetitionGroup competitionGroup) {
        persist(competitionGroup);
    }

    public List<CompetitionGroup> findAll() {
        return list(namedQuery(CompetitionGroup.QUERY_FIND_ALL));
    }

    public CompetitionGroup findById(String id) {
        return get(id);
    }
}
