package com.example.sweater.repositories;

import com.example.sweater.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message,Integer> {
}
