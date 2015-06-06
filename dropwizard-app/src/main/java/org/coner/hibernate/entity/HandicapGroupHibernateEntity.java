package org.coner.hibernate.entity;

import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "handicap_groups")
@NamedQueries({
        @NamedQuery(
                name = HandicapGroupHibernateEntity.QUERY_FIND_ALL,
                query = "from HandicapGroupHibernateEntity"
        )
})
public class HandicapGroupHibernateEntity extends HibernateEntity {
    public static final String QUERY_FIND_ALL = "org.coner.hibernate.entity.HandicapGroupHibernateEntity.findAll";

    private String id;
    private String name;
    private BigDecimal handicapFactor;
    private Set<HandicapGroupSet> handicapGroupSets;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "handicap_factor")
    public BigDecimal getHandicapFactor() {
        return handicapFactor;
    }

    public void setHandicapFactor(BigDecimal handicapFactor) {
        this.handicapFactor = handicapFactor;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "handicap_group_handicap_group_sets",
            joinColumns = {
                    @JoinColumn(name = "handicapGroupId", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "handicapGroupSetId", nullable = false, updatable = false)
            }
    )
    public Set<HandicapGroupSet> getHandicapGroupSets() {
        return handicapGroupSets;
    }

    public void setHandicapGroupSets(Set<HandicapGroupSet> handicapGroupSets) {
        this.handicapGroupSets = handicapGroupSets;
    }
}
