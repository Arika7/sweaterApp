package com.example.sweater.controllers;

import com.example.sweater.models.User;
import com.example.sweater.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String register(){
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
       Optional<User> userFromDb = userService.findByUsername(user.getUsername());
       if(userFromDb.isPresent()){
           model.addAttribute("message", "User already exists");
           return "/auth/registration";
       }
       userService.save(user);
        return "redirect:/login";
    }
}
