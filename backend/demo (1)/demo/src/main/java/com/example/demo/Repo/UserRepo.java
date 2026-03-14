package com.example.demo.Repo;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAll();
}
