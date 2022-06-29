package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.repository.UserRepository;
import de.othr.sw.pumpal.repository.WorkoutRepository;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import de.othr.sw.pumpal.service.exception.WorkoutNotFoundException;
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
import java.util.*;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    //Comments werden durch CascadeType.REMOVE automatisch gelöscht,
    //da es in meinem Fall nicht gewollt ist, diese Entitätsobjekte ohne Referenz auf ein Workout in der DB zu erhalten.
    //Ansonsten müsste man hier den CommentService autowiren und bei Löschen eines
    //Workouts die Referenz aller Kommentare auf dieses Workout entfernen über entsprechende Service Methoden
    //(falls Kommentare  aus irgendwelchen Gründen archiviert werden sollten o.Ä.)


    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserService userService;

    @Autowired
    Logger logger;

    @Override
    @Transactional
    public List<Workout> getAllWorkoutsOfUserByVisibility(Visibility visibility, User user) {
        if (user != null) {
            logger.info("Got workouts of user " + user.getEmail());
            return workoutRepository.findAllByAuthorAndVisibility(user, visibility);
        }
        logger.info("There are no workouts yet by user " + user.getEmail());
        return null;
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
        logger.info("Got saved workouts from user " + user.getEmail());
        return workoutRepository.getSavedWorkoutsOfUserWithEmail(user.getEmail());
    }


    @Override
    public Workout getWorkoutById(Long workoutId) {
        return workoutRepository.findById(workoutId).orElseThrow(
                () -> new WorkoutNotFoundException("Workout with id " + workoutId + " not found")
        );
    }


    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public Workout createWorkout(Workout workout, User user) {
        workout.setAuthor(user);
        workout.setDate(Timestamp.valueOf(LocalDateTime.now()));

        int index =  1;
        for (Exercise exercise : workout.getExercises()) {
            if (exercise.getDescription().isBlank()) continue;
            exercise.setId(index++);
        }
        logger.info("Created workout.");
        return workoutRepository.save(workout);
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void deleteWorkout(Workout workout) {
////        alle savedWorkout Referenzen entfernen!
        if(workout.getSavedBy()!=null) {
            if(workout.getSavedBy().size()>=1) {
                List<User> savedBy = new ArrayList<>(workout.getSavedBy());
                for(User user : savedBy) {
                    userService.removeWorkoutFromSavedWorkoutsFromUser(workout, user);
                }
            }
        }
        workoutRepository.delete(workout);
        logger.info("Deleted workout with id " + workout.getID());
    }

}
