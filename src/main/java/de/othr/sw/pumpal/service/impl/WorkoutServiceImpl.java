package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.repository.UserRepository;
import de.othr.sw.pumpal.repository.WorkoutRepository;
import de.othr.sw.pumpal.service.WorkoutService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

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


    //    @Override
//    public List<Workout> getAllVisibleWorkoutsForUser(User user, List<User> friends) { //find the friends in FriendService beforehand
//        if (user != null) {
//
//        };
//        return null;
//    }

    @Override
    public List<Workout> getNewestWorkouts(Collection<Visibility> visibilities) {
        //check if user is loggedin and has friends
        //if yes: display from public and friends workouts
        //else only from public workouts
        return workoutRepository.findFirst5ByVisibilityInOrderByDateDesc(visibilities);
    }

    @Override
    public List<Workout> getSavedWorkoutsOfUser(User user) {
        return null;
    }

    @Override
    public List<Workout> getWorkoutsOfCertainLevel(Level level) {
        return null;
    }


    @Override
    public Workout getWorkoutById(Long workoutId) {
        return workoutRepository.findById(workoutId).orElseThrow(
                () -> new UserNotFoundException("Workout with id " + workoutId + " not found")
        );
    }

    @Override
    public void createWorkout(Workout workout) {

    }

    @Override
    public Workout saveWorkout(Workout workout, User user) {
        workout.setAuthor(user);
        workout.setDate(Timestamp.valueOf(LocalDateTime.now()));
        return workoutRepository.save(workout);
    }

    @Override
    public void deleteWorkout(Workout workout) {

    }

    @Override
    public void removeWorkoutFromSavedWorkouts(Workout workout, User user) {

    }
}
