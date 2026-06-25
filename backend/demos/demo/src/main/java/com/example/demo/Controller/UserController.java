package com.example.demo.Controller;

import com.example.demo.Entity.Contact;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class UserController {

    private final UserService userService;

    // -------- Get all contacts --------
    @GetMapping("/contacts")
    public ResponseEntity<List<Contact>> getContacts(HttpServletRequest request) {

        String email = (String) request.getAttribute("email");

        log.info("Fetching all contacts for user: {}", email);

        List<Contact> contacts = userService.getContect(email);

        log.info("Found {} contacts for user {}", contacts.size(), email);

        return ResponseEntity.ok(contacts);
    }

    // -------- Get contact by ID --------
    @GetMapping("/contacts/{id}")
    public ResponseEntity<?> getContactById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        try {

            String email = (String) request.getAttribute("email");

            log.info("Fetching contact id {} for user {}", id, email);

            Contact contact = userService.getContactById(id, email);

            log.info("Contact found. ID: {}", id);

            return ResponseEntity.ok(contact);

        } catch (RuntimeException e) {

            log.error("Failed to fetch contact id {}. Reason: {}", id, e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    // -------- Add new contact --------
    @PostMapping("/contacts")
    public ResponseEntity<?> addContact(
            @RequestBody Contact contact,
            HttpServletRequest request
    ) {
        try {

            String email = (String) request.getAttribute("email");

            log.info("Creating contact for user {}", email);
            log.debug("Contact payload: {}", contact);

            contact.setUser(userService.getUserByEmail(email));

            Contact saved = userService.add(contact);

            log.info("Contact created successfully. ID: {}", saved.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (RuntimeException e) {

            log.error("Failed to create contact. Reason: {}", e.getMessage(), e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // -------- Update contact --------
    @PutMapping("/contacts/{id}")
    public ResponseEntity<?> updateContact(
            @PathVariable Long id,
            @RequestBody Contact updatedContact,
            HttpServletRequest request
    ) {
        try {

            String email = (String) request.getAttribute("email");

            log.info("Updating contact {} for user {}", id, email);

            Contact updated =
                    userService.updateContact(id, updatedContact, email);

            log.info("Contact updated successfully. ID: {}", id);

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {

            log.error("Failed to update contact {}. Reason: {}", id, e.getMessage(), e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // -------- Delete contact --------
    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {

        log.info("Deleting contact with id {}", id);

        try {

            userService.delete(id);

            log.info("Contact deleted successfully. ID: {}", id);

            return ResponseEntity.ok("Contact deleted successfully");

        } catch (Exception e) {

            log.error("Failed to delete contact {}. Reason: {}", id, e.getMessage(), e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // -------- Smart search --------
    @GetMapping("/contacts/search")
    public ResponseEntity<List<Contact>> searchContacts(
            @RequestParam String query,
            HttpServletRequest request
    ) {

        String email = (String) request.getAttribute("email");

        log.info("Smart search request by user {}. Query: {}", email, query);

        List<Contact> results = userService.search(query, email);

        log.info("Search completed. Found {} contacts.", results.size());

        return ResponseEntity.ok(results);
    }

    public UserService getUserService() {
        return userService;
    }

}
