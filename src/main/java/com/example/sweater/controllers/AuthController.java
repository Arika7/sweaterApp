package com.example.sweater.controllers;

import com.example.sweater.DTO.CaptchaResponseDTO;
import com.example.sweater.models.User;
import com.example.sweater.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    String secret;
    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/registration")
    public String register(@ModelAttribute("user") User user){
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model,
                          @RequestParam("g-recaptcha-response") String captchaResponse){


        CaptchaResponseDTO response = verifyReCaptcha(captchaResponse);

        if(response != null && !response.isSuccess()){
            model.addAttribute("captchaError", "Please fill the captcha");
        }
        if(user.getPassword()!= null && !user.getPassword().equals(user.getPassword2())){
            bindingResult.rejectValue("password2","password2", "Passwords are different!");
        }
       Optional<User> userFromDb = userService.findByUsername(user.getUsername());
       if(userFromDb.isPresent()){
           model.addAttribute("message", "User already exists");
           return "/auth/registration";
       }
       if(bindingResult.hasErrors()||(response != null && !response.isSuccess())){
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

    public CaptchaResponseDTO verifyReCaptcha(String gRecaptchaResponse){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String > map = new LinkedMultiValueMap<>();
        map.add("secret", secret);
        map.add("response", gRecaptchaResponse);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map,headers);

        CaptchaResponseDTO response = restTemplate.postForObject(CAPTCHA_URL,request,CaptchaResponseDTO.class);

        System.out.println(response);

        return response;
    }

}
