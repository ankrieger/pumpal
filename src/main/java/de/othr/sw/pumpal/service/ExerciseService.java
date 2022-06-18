package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.Exercise;

import java.util.List;

public interface ExerciseService {

    List<Exercise> getAllExercisesOfWorkout(Long workoutId);

    // auf button "Details" für die optionalen weiteren Infos zur Ausführung (String note), maybe einfach String als Rückgabe?
    Exercise viewExerciseDetails(Long exerciseId);

    int getNumberOfExercisesOfWorkout(Long workoutId);

    Exercise setNewExercise(Exercise exercise, Long workoutid);

    void removeExercise(Exercise exercise);
}
