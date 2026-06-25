package com.example.demo.Service;

import com.example.demo.Entity.User;
import com.example.demo.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepo userRepo;

    public UserRepo getUserRepo() {
        log.debug("Returning UserRepo instance");
        return userRepo;
    }


    public List<User> allUsers() {

        log.info("Fetching all users from database");

        List<User> users = userRepo.findAll();

        log.info("Successfully fetched {} users", users.size());

        return users;
    }


    public User getUser(Long id) {

        log.info("Fetching user with ID: {}", id);

        User user = userRepo.findById(id).orElse(null);

        if (user != null) {
            log.info("User found with ID: {}, Email: {}", id, user.getEmail());
        } else {
            log.warn("User not found with ID: {}", id);
        }

        return user;
    }


    public void deleteUser(Long id) {

        log.info("Attempting to delete user with ID: {}", id);

        if (!userRepo.existsById(id)) {
            log.warn("Delete failed. User not found with ID: {}", id);
            return;
        }

        userRepo.deleteById(id);

        log.info("User deleted successfully with ID: {}", id);
    }
}