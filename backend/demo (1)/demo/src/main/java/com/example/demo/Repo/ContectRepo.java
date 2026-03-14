package com.example.demo.Repo;

import com.example.demo.Entity.Contact;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContectRepo extends JpaRepository<Contact,Long> {
    List<Contact> findByUser(User user);
}
