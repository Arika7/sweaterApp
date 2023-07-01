package com.example.sweater.controllers;


import com.example.sweater.models.Message;
import com.example.sweater.models.User;
import com.example.sweater.services.MessageService;
import com.example.sweater.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MessageController {

        @Value("${upload.path}")
        private String uploadPath;

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
    public String main(Model model, @RequestParam(name = "filter", required = false) String filter, Message message){
        model.addAttribute("message1", new Message());
        if(filter != null) model.addAttribute("messages", messageService.findByTag(filter));
        if(filter == null || filter.isEmpty()) model.addAttribute("messages",messageService.findAll());
        if(filter != null && filter.isBlank()) return "redirect:/main";
        model.addAttribute("filterr", filter);
        return "main";
}

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user , @ModelAttribute("message") @Valid Message message, BindingResult bindingResult,  Model model, @RequestParam("file") MultipartFile file) throws IOException {
        model.addAttribute("messageText", message.getText());
        model.addAttribute("messageTag", message.getTag());
        if(bindingResult.hasErrors()){
            model.addAttribute("messages",messageService.findAll());
            return "/main";
        }else {
        if(file != null && !file.getOriginalFilename().isEmpty()){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName= uuidFile+ "." + fileName;
        message.setFilename(resultFileName);
        String uploadDir= "uploads";
        FileUploadUtil.saveFile(uploadDir,resultFileName,file);}
        message.setAuthor(user);
        messageService.save(message);
        return "redirect:/main";}
}
}
