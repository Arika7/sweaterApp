package com.example.sweater.services;

import com.example.sweater.models.Role;
import com.example.sweater.models.User;
import com.example.sweater.repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public void save(User user){
        user.setActive(true);
        user.setRoles(Collections.singletonList(Role.USER));
        userRepo.save(user);
    }
}
