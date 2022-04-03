package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Comment extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "email", name="user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name="workout_id", nullable = false)
    private Workout workout;


    public Comment() {

    }

    public Comment(Long id, User author, String comment, Date date, Workout workout) {
        this.id = id;
        this.author = author;
        this.comment = comment;
        this.date = date;
        this.workout = workout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }


    @Override
    public Long getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author=" + author +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                ", workout=" + workout +
                '}';
    }
}
