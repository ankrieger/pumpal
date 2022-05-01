package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.Basic;
import javax.persistence.*;
import java.util.*;

@Entity
public class Workout extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(referencedColumnName = "email", name="user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;


    @OneToMany(mappedBy = "workout")
    private List<Exercise> exercises;

    private String tagString;

    @OneToMany(mappedBy = "workout")
    private List<Comment> comments;

    @ManyToMany(mappedBy = "savedWorkouts")
    private List<User> savedBy;

    @Enumerated(value = EnumType.STRING)
    private Level level;

    @Transient
    private List<String> tags;
//    bei initialisierung: tags splitten und in ein Array packen

    public Workout() {

    }

    public Workout(Long id, String title, Date date, User author, String description,
                   Visibility visibility, List<Exercise> exercises, Level level, String tagString) {  //Tags noch rein: falls != 0 => Splice
        this.id = id;
        this.title = title;
        this.date = date;
        this.author = author;
        this.description = description;
        this.visibility = visibility;
        this.exercises = exercises;
        this.level = level;
        this.tagString = (tagString != null && !tagString.isEmpty()) ? tagString : "";
        this.tags = new ArrayList<String>(Arrays.asList(this.tagString.split(/*"\\s*,\\s*"*/";")));
    }

    public Workout(Long id, String title, Date date, User author, String description,
                   Visibility visibility, List<Exercise> exercises, String tagString,
                   List<Comment> comments, Level level) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.author = author;
        this.description = description;
        this.visibility = visibility;
        this.exercises = exercises;
        this.comments = comments;
        this.level = level;
        this.tagString = (tagString != null && !tagString.isEmpty()) ? tagString : "";
        this.tags = new ArrayList<String>(Arrays.asList(this.tagString.split("\\s*,\\s*")));

    }

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

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<String> getTags() {
        return tags;
    }

    // do I need this?
    public void setTags(List<String> tags) {
        this.tags = tags;
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
                ", author=" + author +
                ", description='" + description + '\'' +
                ", exercises=" + exercises +
                ", tagString='" + tagString + '\'' +
                ", comments=" + comments +
                ", level=" + level +
                ", visibility=" + visibility +
                '}';
    }
}
