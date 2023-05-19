package com.example.sweater.services;

import com.example.sweater.models.User;
import com.example.sweater.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServ implements UserDetailsService {

    private final UserRepo userRepo;
    @Autowired
    public UserDetailsServ(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isEmpty()) throw  new UsernameNotFoundException("Username not found!");
        return user.get();
    }
}
