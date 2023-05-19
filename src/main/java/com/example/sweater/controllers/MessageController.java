package com.example.sweater.controllers;


import com.example.sweater.models.Message;
import com.example.sweater.models.User;
import com.example.sweater.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {


    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
public String hello(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model){
    model.addAttribute("name", name);
    return "/auth/hello";
}

    @GetMapping("/main")
    public String main(Model model, @RequestParam(name = "filter", required = false) String filter){
        model.addAttribute("message", new Message());
        if(filter != null) model.addAttribute("messages", messageService.findByTag(filter));
        if(filter == null || filter.isEmpty()) model.addAttribute("messages",messageService.findAll());
        if(filter != null && filter.isBlank()) return "redirect:/main";
        model.addAttribute("filterr", filter);
        return "main";
}

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user , @ModelAttribute("message") Message message, Model model){

        messageService.save(message);
        return "redirect:/main";
}
}
