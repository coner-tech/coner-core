package org.axrunner.hibernate.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *
 */
@Entity
@Table(name = "events")
@NamedQueries({
        @NamedQuery(
                name = Event.QUERY_FIND_ALL,
                query = "from Event"
        )
})
public class Event extends HibernateEntity {

    private String id;
    private String name;
    private Date date;

    public static final String QUERY_FIND_ALL = "org.axrunner.hibernate.entity.Event.findAll";

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

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
