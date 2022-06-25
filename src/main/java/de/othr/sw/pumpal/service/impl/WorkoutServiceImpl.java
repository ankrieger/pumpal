package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.repository.UserRepository;
import de.othr.sw.pumpal.repository.WorkoutRepository;
import de.othr.sw.pumpal.service.WorkoutService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Logger logger;

    @Override
    @Transactional
    public List<Workout> getAllWorkoutsOfUserByVisibility(Visibility visibility, User user) {
        if (user != null) {
            //anders formulieren? ohne auf user repo zuzugreifen?
            //return userRepository.findById(user.getEmail()).get().getWorkouts();
            return workoutRepository.findAllByAuthorAndVisibility(user, visibility);
        }
        return null;
    }


    @Override
    public List<Workout> getAllWorkoutsVisible(Collection<Visibility> visibilities) {
        return workoutRepository.findAllByVisibilityInOrderByDateDesc(visibilities);
    }

    @Override
    public Page<Workout> findWorkoutPage(Collection<Visibility> visibilities, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,10);
        return workoutRepository.findAllByVisibilityInOrderByDateDesc(visibilities, pageable);
    }

    @Override
    public Page<Workout> findFilteredWorkoutPage(String filter, List<Level> levels, List<Visibility> visibilities, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,10);
        if (filter.isBlank()) {
            return workoutRepository.getWorkoutsByKeyword("", levels, visibilities, pageable);
        }
        return workoutRepository.getWorkoutsByKeyword(filter, levels, visibilities, pageable);
    }

    @Override
    public List<Workout> getNewestWorkouts(Collection<Visibility> visibilities) {
        //only public workouts in this section
        logger.info("Getting the first 5 workouts");
        return workoutRepository.findFirst5ByVisibilityInOrderByDateDesc(visibilities);
    }

    @Override
    public List<Workout> getSavedWorkoutsOfUser(User user) {
        return workoutRepository.getSavedWorkoutsOfUserWithEmail(user.getEmail());
    }


    @Override
    public Workout getWorkoutById(Long workoutId) {
        return workoutRepository.findById(workoutId).orElseThrow(
                () -> new UserNotFoundException("Workout with id " + workoutId + " not found")
        );
    }


    @Override
    public Workout createWorkout(Workout workout, User user) {
        workout.setAuthor(user);
        workout.setDate(Timestamp.valueOf(LocalDateTime.now()));

        int index =  1;
        for (Exercise exercise : workout.getExercises()) {
            if (exercise.getDescription().isBlank()) continue;
            exercise.setId(index++);
        }
        return workoutRepository.save(workout);
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void deleteWorkout(Workout workout) {
//        alle savedWorkout Referenzen entfernen!
        if(workout.getSavedBy()!=null) {
            if(workout.getSavedBy().size()>=1) {
                List<User> savedby = new ArrayList<>(workout.getSavedBy());
                for(User u : savedby) {
                    workout.removeUserSavedBy(u);
                    List<Workout> savedWorkouts = u.getSavedWorkouts();
                    savedWorkouts.remove(workout);
                    u.setSavedWorkouts(savedWorkouts);
                }
            }
        }
        workoutRepository.delete(workout);
    }


}
