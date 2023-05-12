package com.example.sweater.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
@GetMapping("/hello2")
public String hello(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model){
    model.addAttribute("name", name);
    return "hello";
}

@GetMapping
    public String main(Model model){
    model.addAttribute("text","Main page");
    return "main";
}
}
