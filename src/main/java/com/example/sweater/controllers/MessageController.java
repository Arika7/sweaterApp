package com.example.sweater.controllers;


import com.example.sweater.models.Message;
import com.example.sweater.models.User;
import com.example.sweater.services.MessageService;
import com.example.sweater.services.UserService;
import com.example.sweater.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MessageController {

        @Value("${upload.path}")
        private String uploadPath;

    private final MessageService messageService;
    private final UserService  userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
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
            addPhoto(message, file);
        }
        message.setAuthor(user);
        messageService.save(message);
        return "redirect:/main";}
}

    @GetMapping("/my-Messages")
    public String getMessages(@AuthenticationPrincipal User user , Model model){
        User owner = userService.findById(user.getId()).get();
        List<Message> messageList = owner.getMessage();
        model.addAttribute("messages", messageList);
        return "myMessages";
    }

    @GetMapping("/user-messages/{id}")
    public String getUserMessages(Model model,@PathVariable("id")int id){

        List<Message> message= userService.findById(id).get().getMessage();
        model.addAttribute("messages", message);
        return "myMessages";
    }

    @GetMapping("/my-Messages/edit/{id}")
    public String editUserMessages(Model model,@PathVariable("id")int id){

        Message message = messageService.findById(id);
        model.addAttribute("myMessage", message);
        model.addAttribute("message", message);
        model.addAttribute("messageText", message.getText());
        model.addAttribute("messageTag", message.getTag());
        return "editMessage";
    }

    @PostMapping("/edit/{id}")
    public String editMessage(@AuthenticationPrincipal User user ,@PathVariable("id") int id, @ModelAttribute("message") @Valid Message message, BindingResult bindingResult,  Model model, @RequestParam("file") MultipartFile file) throws IOException {
        if(bindingResult.hasErrors()){
             return "redirect:/edit/"+ id;
        }else {
            if(file != null && !file.getOriginalFilename().isEmpty()){
                addPhoto(message, file);
            }
            message.setAuthor(user);
            messageService.update(id,message);
            return "redirect:/my-Messages";}}

    private static void addPhoto(Message message, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName= uuidFile+ "." + fileName;
        message.setFilename(resultFileName);
        String uploadDir= "uploads";
        FileUploadUtil.saveFile(uploadDir,resultFileName, file);
    }


}
