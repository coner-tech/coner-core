package org.coner.hibernate.entity;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "registrations")
@NamedQueries({
        @NamedQuery(
                name = RegistrationHibernateEntity.QUERY_FIND_ALL_WITH_EVENT,
                query = "FROM RegistrationHibernateEntity r "
                        + "WHERE r.event.id = :" + RegistrationHibernateEntity.PARAMETER_EVENT_ID
        )
})
public class RegistrationHibernateEntity extends HibernateEntity {

    public static final String QUERY_FIND_ALL_WITH_EVENT = "org.coner.hibernate.entity.RegistrationHibernateEntity"
            + ".findAllWithEvent";
    public static final String PARAMETER_EVENT_ID = "eventId";

    private String id;
    private String firstName;
    private String lastName;
    private EventHibernateEntity event;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToOne
    public EventHibernateEntity getEvent() {
        return event;
    }

    public void setEvent(EventHibernateEntity event) {
        this.event = event;
    }
}
