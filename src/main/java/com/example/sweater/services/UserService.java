package com.example.sweater.services;

import com.example.sweater.models.Role;
import com.example.sweater.models.User;
import com.example.sweater.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService{

    private final UserRepo userRepo;
    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> findByUsername(String username){
        return userRepo.findByUsername(username);
    }
    @Transactional
    public void save(User user){
        user.setActive(true);
        user.setRoles(Collections.singletonList(Role.USER));
        userRepo.save(user);
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public Optional<User> findById(int id){return userRepo.findById(id);}

    @Transactional
    public void update(int id, User updatedUser){
        updatedUser.setId(id);
        userRepo.save(updatedUser);
    }
}
