package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Comment extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "email", name="user_id")
    private User author;

    @Column(nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name="workout_id")
    private Workout workout;


    public Comment() {

    }

//    public Comment(User author, Workout workout) {
//        this.author = author;
//        this.workout = workout;
//    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", description='" + description + '\'' +
                ", date=" + date +
                ", workout=" + workout +
                '}';
    }
}
