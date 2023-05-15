package com.example.sweater.services;

import com.example.sweater.models.Message;
import com.example.sweater.repositories.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepo messageRepo;
    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public List<Message> findAll(){return messageRepo.findAll();}

    public List<Message> findByTag(String tag){ return messageRepo.findByTag(tag);}
    @Transactional
    public void save(Message message){messageRepo.save(message);}
}
