package com.example.demo.Controller;

import com.example.demo.Entity.Contact;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
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
        return ResponseEntity.ok(userService.getContect(email));
    }

    // -------- Get contact by ID --------
    @GetMapping("/contacts/{id}")
    public ResponseEntity<?> getContactById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        try {
            String email = (String) request.getAttribute("email");
            Contact contact = userService.getContactById(id, email);
            return ResponseEntity.ok(contact);

        } catch (RuntimeException e) {
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
            contact.setUser(userService.getUserByEmail(email));

            Contact saved = userService.add(contact);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (RuntimeException e) {
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
            Contact updated = userService.updateContact(id, updatedContact, email);
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // -------- Delete contact --------
    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("Contact deleted successfully");
    }

    // -------- Smart search --------
    @GetMapping("/contacts/search")
    public ResponseEntity<List<Contact>> searchContacts(
            @RequestParam String query,
            HttpServletRequest request
    ) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(userService.search(query, email));
    }
}
