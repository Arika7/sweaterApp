package com.example.sweater.controllers;


import com.example.sweater.models.Role;
import com.example.sweater.models.User;
import com.example.sweater.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users",userService.findAll());
        return "userList";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public String userEditForm(@PathVariable("id") int id, Model model){
        Optional<User> user = userService.findById(id);

        if(user.isEmpty()) model.addAttribute("error","");
        if(user.isPresent()) {model.addAttribute("user", userService.findById(id).get());
            model.addAttribute("roles", Role.values());
        }

        return "userEdit";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    public String userSave(@ModelAttribute("user") User user, @PathVariable("id")int id){
        userService.saveUser(user, id);
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email",user.getEmail());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User authUser , @ModelAttribute("user") User user){
        userService.updateProfile(authUser, user);
        return "redirect:/users/profile";
    }
}
