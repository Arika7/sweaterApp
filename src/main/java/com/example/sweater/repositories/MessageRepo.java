package com.example.sweater.repositories;

import com.example.sweater.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepo extends JpaRepository<Message,Integer> {

    List<Message> findByTag(String tag);

}
