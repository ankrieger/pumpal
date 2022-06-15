package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    Page<User> findUserPage(int pageNumber);

    User getUserByEmail(String email);

    User registerUser(User user);

    User updateUser(User user);

    void deleteUser(User user);


}
