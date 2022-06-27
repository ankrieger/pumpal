package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Friendship extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requesting_user", referencedColumnName = "email", nullable = false)
    private User requestingUser;

    @ManyToOne
    @JoinColumn(name = "requested_user", referencedColumnName = "email", nullable = false)
    private User requestedUser;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private boolean isActive;

    public Friendship() {

    }
//
//    public Friendship(Long id, User requestingUser, User requestedUser, Date date, boolean isActive) {
//        this.id = id;
//        this.requestingUser = requestingUser;
//        this.requestedUser = requestedUser;
//        this.date = date;
//        this.isActive = isActive;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRequesting() {
        return requestingUser;
    }

    public void setRequesting(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public User getRequested() {
        return requestedUser;
    }

    public void setRequested(User requestedUser) {
        this.requestedUser = requestedUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    @Override
    public Long getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", requestingUser=" + requestingUser +
                ", requestedUser=" + requestedUser +
                ", date=" + date +
                ", isActive=" + isActive +
                '}';
    }
}
