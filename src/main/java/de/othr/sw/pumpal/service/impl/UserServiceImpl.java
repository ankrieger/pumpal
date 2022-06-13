package de.othr.sw.pumpal.service.impl;

import de.othr.sw.pumpal.entity.AccountType;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.repository.UserRepository;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
    public User updateUser(User user) {
        return null;
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
}
