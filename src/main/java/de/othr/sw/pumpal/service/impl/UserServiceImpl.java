package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.repository.UserRepository;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {



    //Friendships, Workouts und Comments werden durch CascadeType.REMOVE automatisch gelöscht,
    //da es in meinem Fall nicht gewollt ist, diese Entitätsobjekte ohne Referenz auf einen User in der DB zu erhalten.
    //Ansonsten müsste man hier die entsprechenden Services autowiren und bei Löschen eines
    //Users die Referenz aller Kommentare,Workouts und firendships auf diesen User entfernen über entsprechende Service Methoden
    //(z.B. falls Kommentare oder Workouts trotz gelöschtem User für andere einsehbar bleiben sollten)

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Logger logger;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public Page<User> findFilteredUser(String filter, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,10);
        if (filter.isBlank()) {
            return userRepository.getUserByKeyword("", pageable);
        }
        return userRepository.getUserByKeyword(filter, pageable);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findById(email).orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found")
        );

    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public User registerUser(User user) {
        //AccountType setzen
        if (user.getAccountType() != AccountType.ADMIN)
            user.setAccountType(AccountType.USER);

        user.setPassword(passwordEncoder.encode(user.getPassword())); //hashen des Passwords
        logger.info("Register user with id " + user.getEmail());
        return userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User updateUser(User user, User newAttributes) {

        user.setFirstName(newAttributes.getFirstName());
        user.setName(newAttributes.getName());
        user.setAddress(newAttributes.getAddress());
        user.setDescription(newAttributes.getDescription());

        logger.info("Updated user with id " + user.getEmail());
        return userRepository.save(user);
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void deleteUser(User user) {
        User userToDelete = userRepository.findById(user.getID()).get();
        removeAllSavingUsersFromUsersWorkouts(userToDelete);
        userRepository.delete(userToDelete);
        logger.info("Deleted user with id " + user.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " not found")
        );
    }

    @Override
    public List<User> getAllUsersSavingWorkout(Workout workout) {
        logger.info("Got all users saving workout with id " + workout.getID());
        return userRepository.getAllUsersSavingWorkoutWithId(workout.getId());
    }



    @Override
    @Transactional( propagation = Propagation.REQUIRED)
    public void removeAllSavingUsersFromUsersWorkouts(User user) {
        User userToDelete = userRepository.findById(user.getID()).get();
        List<Workout> workouts = new ArrayList<>(userToDelete.getWorkouts());
        if(user!=null && workouts!=null) {
            for(Workout workout:workouts) {
                List<User> savingUsers = new ArrayList<>(workout.getSavedBy());
                if(savingUsers!=null) {
                    for(User userSaved: savingUsers) {
                        removeWorkoutFromSavedWorkoutsFromUser(workout,userSaved);
                    }
                }
            }
        }
        logger.info("Removed all user saving references to workouts from user " + user.getEmail());
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void removeWorkoutFromSavedWorkoutsFromUser(Workout workout, User user) {
        User userSave = userRepository.findById(user.getID()).get();
        List<Workout> savedWorkouts = new ArrayList<>(userSave.getSavedWorkouts());
        if(user!=null && workout!=null && savedWorkouts!=null){
            if(savedWorkouts.contains(workout)) {
                savedWorkouts.remove(workout);
                workout.removeUserSavedBy(userSave);
            }

        userSave.setSavedWorkouts(savedWorkouts);
        userRepository.save(userSave);
        logger.info("Removed workout with id " + workout.getID() + " for user " + user.getEmail() + " from saved workouts.");

        }
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void saveWorkoutForUser(Workout workout, User user) {
        User userSave = userRepository.findById(user.getID()).get();
        List<Workout> savedWorkouts = new ArrayList<>(userSave.getSavedWorkouts());
        if(user!=null && workout!=null){
            if(!savedWorkouts.contains(workout)) {
                savedWorkouts.add(workout);
                workout.addUserSavedBy(userSave);
            }
        }
        userSave.setSavedWorkouts(savedWorkouts);
        userRepository.save(userSave);
        logger.info("Saved workout with id " + workout.getID() + " for user " + user.getEmail());
    }

}
