package com.example.demo.Service;

import com.example.demo.Entity.User;
import com.example.demo.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepo userRepo;
 public UserRepo getUserRepo() {
        return userRepo;
    }
    // ✅ RETURN ALL USERS
    public List<User> allUsers() {
        return userRepo.findAll();
    }

    public User getUser(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
