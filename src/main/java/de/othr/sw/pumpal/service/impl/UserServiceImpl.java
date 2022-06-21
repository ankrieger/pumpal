package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.repository.UserRepository;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
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

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public Page<User> findUserPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,10);

        User user_auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findAllByEmailNotLike(user_auth.getEmail(), pageable);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findById(email).orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found")
        );

    }

    @Override
    public User registerUser(User user) {
        //AccountType setzen
        if (user.getAccountType() != AccountType.ADMIN)
            user.setAccountType(AccountType.USER);

        user.setPassword(passwordEncoder.encode(user.getPassword())); //hashen des Passwords
        return userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User updateUser(User user, User newAttributes) {

        user.setFirstName(newAttributes.getFirstName());
        user.setName(newAttributes.getName());
        user.setAddress(newAttributes.getAddress());
        user.setDescription(newAttributes.getDescription());


        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " not found")
        );
    }

    @Override
    public List<User> getAllUsersSavingWorkout(Workout workout) {
        return userRepository.getAllUsersSavingWorkoutWithId(workout.getId());
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void removeWorkoutFromSavedWorkoutsFromUser(Workout workout, User user) {
        User userSave = userRepository.findById(user.getID()).get();
        List<Workout> savedWorkouts = userSave.getSavedWorkouts();
        if(user!=null && workout!=null && savedWorkouts!=null){
            if(savedWorkouts.contains(workout)) {
                savedWorkouts.remove(workout);
//                userSave.setSavedWorkouts(savedWorkouts);
                workout.removeUserSavedBy(userSave);
                userRepository.save(userSave);
            }
        }
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void saveWorkoutForUser(Workout workout, User user) {
        User userSave = userRepository.findById(user.getID()).get();
        List<Workout> savedWorkouts = userSave.getSavedWorkouts();
        if(user!=null && workout!=null){
            if(!savedWorkouts.contains(workout)) {
                savedWorkouts.add(workout);
//                userSave.setSavedWorkouts(savedWorkouts);
                workout.addUserSavedBy(userSave);
                userRepository.save(userSave);
            }
        }
    }

}
