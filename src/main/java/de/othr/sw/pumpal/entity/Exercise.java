package de.othr.sw.pumpal.entity;

import de.othr.sw.pumpal.entity.util.SingleIdEntity;

import javax.persistence.*;

@Embeddable
public class Exercise {

    private int id;

    private String description;

    private int sets;

    private int reps;

    private int weight;

    private String note;



    public Exercise() {
    }

    public Exercise(String description, int sets, int reps, int weight, String note) {
        this.description = description;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                ", note='" + note + '\'' +
                '}';
    }

}
