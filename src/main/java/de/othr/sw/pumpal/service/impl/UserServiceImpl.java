package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.AccountType;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.repository.UserRepository;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findById(email).orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found")
        );

    }

    @Override
    public User registerUser(User user) {
        //user.setPassword(user.getPassword());
        if (user.getAccountType() != AccountType.ADMIN)
            user.setAccountType(AccountType.USER);

        System.out.println(user);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }
}
