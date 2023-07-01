package com.example.sweater.controllers;

import com.example.sweater.models.User;
import com.example.sweater.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String register(@ModelAttribute("user") User user){
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model){
        if(user.getPassword()!= null && !user.getPassword().equals(user.getPassword2())){
            bindingResult.rejectValue("password2","password2", "Passwords are different!");
        }
       Optional<User> userFromDb = userService.findByUsername(user.getUsername());
       if(userFromDb.isPresent()){
           model.addAttribute("message", "User already exists");
           return "/auth/registration";
       }
       if(bindingResult.hasErrors()){
           return "/auth/registration";
       }
       userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String active(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if(isActivated) model.addAttribute("messageS", "User successfully activated");
        else{ model.addAttribute("messageN", "Activation code is not found");}
        return "/auth/login";
    }
}
