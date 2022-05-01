package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.Level;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Workout;

import java.util.List;

public interface WorkoutService {
    //sowohl für eigene workouts als auch für die anderer user als methode
    List<Workout> getAllWorkoutsOfUser(User user); //oder String Email; nicht vergessen entsprechend visibility!

    List<Workout> getNewestWorkouts();  //auf startpage, noch vor dem einloggen angezeigt, so 5 Titel + Descprition -> bei Klick auf Titel zu Einloggen weitergeleitet

    List<Workout> getSavedWorkoutsOfUser(User user);

    List<Workout> getWorkoutsOfCertainLevel(Level level); // String level?

//    List<Workout> getWorkoutsContainingCategories(List<String> tags);  falls Suche möglich ist

    void createWorkout(Workout workout);

    void saveWorkout(Workout workout, User user);

    void deleteWorkout(Workout workout);

    void removeWorkoutFromSavedWorkouts(Workout workout, User user);
}
