package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.Basic;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Workout extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 60, message = "Your workout's title must be between 3 and 60 characters long")
    @NotEmpty(message = "Workout title cannot be empty")
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(referencedColumnName = "email", name="user_id"  /*FetchType eager?*/)
    private User author;

    @Size(min = 2, max = 250, message = "Your workout's description must be between 2 and 250 characters long")
    @NotEmpty(message = "Workout description cannot be empty")
    private String description;

    private Integer durationInMin;

    @Enumerated(value = EnumType.STRING)
    private Visibility visibility;


    @ElementCollection
    @AttributeOverride(name = "description", column = @Column(name = "exercise_description"))
    @AttributeOverride(name = "id", column = @Column(name = "exercise_id"))
    private List<Exercise> exercises;

//    OrphanRemovel => abkoppeln der Kommentare von dem enstprechenden Workout führt zum löschen der Kommentare
    @OneToMany(mappedBy = "workout", orphanRemoval = true,  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ManyToMany(mappedBy = "savedWorkouts", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<User> savedBy;

    @Enumerated(value = EnumType.STRING)
    private Level level;


    public Workout() {

    }

//
//    public Workout(Long id, String title, Date date, User author, String description,
//                   Visibility visibility, List<Exercise> exercises, Level level) {
//        this.id = id;
//        this.title = title;
//        this.date = date;
//        this.author = author;
//        this.description = description;
//        this.visibility = visibility;
//        this.exercises = exercises;
//        this.level = level;
//    }
//
//    public Workout(Long id, String title, Date date, User author, String description,
//                   Visibility visibility, List<Exercise> exercises, Integer durationInMin,
//                   List<Comment> comments, Level level) {
//        this.id = id;
//        this.title = title;
//        this.date = date;
//        this.author = author;
//        this.description = description;
//        this.visibility = visibility;
//        this.exercises = exercises;
//        this.comments = comments;
//        this.level = level;
//        this.durationInMin = durationInMin;
//
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<User> getSavedBy() {
        return Collections.unmodifiableList(savedBy);
    }

    public void setSavedBy(List<User> savedBy) {
        this.savedBy = savedBy;
    }

    public Integer getDurationInMin() {
        return durationInMin;
    }

    public void setDurationInMin(Integer durationInMin) {
        this.durationInMin = durationInMin;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }


    @Override
    public Long getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
//                ", author=" + author +
                ", description='" + description + '\'' +
//                ", exercises=" + exercises +
                ", durationInMin='" + durationInMin + '\'' +
//                ", comments=" + comments +
                ", level=" + level +
                ", visibility=" + visibility +
                '}';
    }

    public void addUserSavedBy(User user) {
        if(!savedBy.contains(user)) savedBy.add(user);
    }

    public void removeUserSavedBy(User user) {
        if(savedBy.contains(user)) savedBy.remove(user);
    }

}
