package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.*;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface WorkoutService {

    List<Workout> getAllWorkoutsOfUserByVisibility(Visibility visibility, User user); //oder String Email; nicht vergessen entsprechend visibility!

    Page<Workout> findFilteredWorkoutPage(String filter, List<Level> levels, List<Visibility> visibilities, int pageNumber);

    List<Workout> getNewestWorkouts(Collection<Visibility> visibilities);  //auf startpage, noch vor dem einloggen angezeigt, so 5 Titel + Descprition -> bei Klick auf Titel zu Einloggen weitergeleitet

    List<Workout> getSavedWorkoutsOfUser(User user);

    Workout getWorkoutById(Long workoutId);

    Workout createWorkout(Workout workout, User user);

    void deleteWorkout(Workout workout);




    //List<Workout> getFilteredWorkouts(String filter, Level level);

//    List<Workout> getAllWorkoutsVisible(Collection<Visibility> visibilities); //ergo alle Public vorerst; später: auch FRIENDS + Freund

//    Page<Workout> findWorkoutPage(Collection<Visibility> visibilities, int pageNumber);


}
