package com.example.sweater.controllers;


import com.example.sweater.models.Role;
import com.example.sweater.models.User;
import com.example.sweater.repositories.UserRepo;
import com.example.sweater.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users",userService.findAll());
        return "userList";
    }

    @GetMapping("/{id}")
    public String userEditForm(@PathVariable("id") int id, Model model){
        Optional<User> user = userService.findById(id);

        if(user.isEmpty()) model.addAttribute("error","");
        if(user.isPresent()) {model.addAttribute("user", userService.findById(id).get());
            model.addAttribute("roles", Role.values());
        }

        return "userEdit";
    }

    @PostMapping("/{id}")
    public String userSave(@ModelAttribute("user") User user, @PathVariable("id")int id){
        User oldUser = userService.findById(id).get();
        oldUser.setUsername(user.getUsername());
        oldUser.setRoles(user.getRoles());
        userService.update(id,oldUser);
        return "redirect:/users";
    }
}
