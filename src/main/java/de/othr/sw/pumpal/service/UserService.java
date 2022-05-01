package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.User;

import java.util.List;

public interface UserService {

    User getUserByEmail(String email);

    User registerUser(User user);

    User updateUser(User user);

    void deleteUser(User user);
}
