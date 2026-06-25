package com.example.demo.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    // ================= GET ALL USERS =================
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        log.info("Admin requested all users");

        List<User> users = adminService.allUsers();

        if (users.isEmpty()) {

            log.warn("No users found in database");

            return ResponseEntity.noContent().build();
        }

        log.info("Successfully returned {} users", users.size());

        return ResponseEntity.ok(users);
    }

    // ================= GET USER BY ID =================
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        log.info("Admin requested user with ID={}", id);

        User user = adminService.getUser(id);

        if (user == null) {

            log.warn("User not found with ID={}", id);

            return ResponseEntity.notFound().build();
        }

        log.info(
                "User found. ID={}, Email={}",
                user.getId(),
                user.getEmail()
        );

        return ResponseEntity.ok(user);
    }

    // ================= DELETE USER =================
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        log.info("Admin requested delete for user ID={}", id);

        User user = adminService.getUser(id);

        if (user == null) {

            log.warn(
                    "Delete failed. User not found with ID={}",
                    id
            );

            return ResponseEntity.notFound()
                    .build();
        }

        adminService.deleteUser(id);

        log.info(
                "User deleted successfully. ID={}, Email={}",
                user.getId(),
                user.getEmail()
        );

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }

    public AdminService getAdminService() {

        log.debug("Returning AdminService instance");

        return adminService;
    }
}