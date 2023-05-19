package com.example.sweater.controllers;


import com.example.sweater.models.User;
import com.example.sweater.repositories.UserRepo;
import com.example.sweater.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
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
        model.addAttribute("user", userService.findById(id));

        return "userEdit";
    }
}
