package com.example.sweater.repositories;

import com.example.sweater.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(int id);

    Optional<User> findByActivationCode(String code);
}
