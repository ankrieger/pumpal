package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.Exercise;
import de.othr.sw.pumpal.repository.ExerciseRepository;
import de.othr.sw.pumpal.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public List<Exercise> getAllExercisesOfWorkout(Long workoutId) {
        return null;
    }

    @Override
    public Exercise viewExerciseDetails(Long exerciseId) {
        return null;
    }

    @Override
    public int getNumberOfExercisesOfWorkout(Long workoutId) {
        return 0;
    }

    @Override
    public Exercise setNewExercise(Exercise exercise, Long workoutid) {
        //evtl workoutId noch setzen?
        return exerciseRepository.save(exercise);
    }

    @Override
    public void removeExercise(Exercise exercise) {

    }
}
