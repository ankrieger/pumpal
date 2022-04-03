package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.*;

@Entity
public class Exercise extends SingleIdEntity<Long> {
    //abschauen, wie so eine liste wo man Sachen dazu addiert und entfernen kann bei Erstellung/Bearbeitung
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name="workout_id", nullable = false)
    private Workout workout;

    @Column(nullable = false)
    private String description;

    private int sets;

    private int reps;

    private int weight;

    private String note;



    public Exercise() {

    }

    public Exercise(Long id, Workout workout, String description) {
        this.id = id;
        this.workout = workout;
        this.description = description;
    }

    public Exercise(Long id, Workout workout, String description, int sets, int reps, int weight, String note) {
        this.id = id;
        this.workout = workout;
        this.description = description;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.note = note;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", workout=" + workout +
                ", description='" + description + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public Long getID() {
        return this.id;
    }
}
