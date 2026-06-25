package com.example.demo.Service;

import com.example.demo.Ai.AiService;
import com.example.demo.Entity.Contact;
import com.example.demo.Entity.User;
import com.example.demo.Repo.ContectRepo;
import com.example.demo.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final ContectRepo contectRepo;
    private final AiService aiService;

    public UserRepo getUserRepo() {
        log.debug("Returning UserRepo instance");
        return userRepo;
    }

    public ContectRepo getContectRepo() {
        log.debug("Returning ContactRepo instance");
        return contectRepo;
    }

    public AiService getAiService() {
        log.debug("Returning AiService instance");
        return aiService;
    }

    // ---------------------- Get all contacts for a user ----------------------
    public List<Contact> getContect(String email) {

        log.info("Fetching contacts for user email={}", email);

        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {
            log.warn("User not found while fetching contacts. email={}", email);
            return List.of();
        }

        List<Contact> contacts = contectRepo.findByUser(user);

        log.info("Fetched {} contacts for user={}", contacts.size(), email);

        return contacts;
    }

    // ---------------------- Get contact by ID ----------------------
    public Contact getContactById(Long id, String email) {

        log.info("Fetching contact id={} for user={}", id, email);

        Contact contact = contectRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Contact not found. id={}", id);
                    return new RuntimeException("Contact not found");
                });

        if (!contact.getUser().getEmail().equals(email)) {
            log.warn("Unauthorized access attempt. ContactId={}, RequestedBy={}",
                    id, email);
            throw new RuntimeException("Access denied");
        }

        log.info("Contact fetched successfully. id={}", id);

        return contact;
    }

    // ---------------------- Get User by Email ----------------------
    public User getUserByEmail(String email) {

        log.info("Fetching user by email={}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found. email={}", email);
                    return new RuntimeException("User not found");
                });

        log.info("User found. email={}, id={}", user.getEmail(), user.getId());

        return user;
    }

    // ---------------------- Add new contact with AI ----------------------
    public Contact add(Contact contact) {

        log.info("Adding new contact. email={}, name={}",
                contact.getEmail(),
                contact.getName());

        // Duplicate check
        log.info("Running AI duplicate detection");

        boolean isDuplicate = aiService.duplicate(contact);

        if (isDuplicate) {
            log.warn("Duplicate contact detected by AI. email={}",
                    contact.getEmail());
            throw new RuntimeException("Duplicate contact detected by AI");
        }

        log.info("AI duplicate check passed");

        // Auto-tagging
        log.info("Generating AI tags");

        String tags = aiService.autotag(
                contact.getName(),
                contact.getJobTitle(),
                contact.getCompany()
        );

        contact.setTags(tags);

        log.info("Generated tags={}", tags);

        // Save
        Contact savedContact = contectRepo.save(contact);

        log.info("Contact saved successfully. id={}",
                savedContact.getId());

        // Insights
        log.info("Generating AI insights");

        String insights = aiService.generateInsights(savedContact);

        savedContact.setTags(savedContact.getTags() + ", " + insights);

        log.info("Generated insights={}", insights);

        return savedContact;
    }

    // ---------------------- Update contact ----------------------
    public Contact updateContact(Long id,
                                 Contact updatedContact,
                                 String email) {

        log.info("Updating contact id={} by user={}",
                id,
                email);

        Optional<Contact> optionalContact =
                contectRepo.findById(id);

        if (optionalContact.isEmpty()) {
            log.error("Contact not found. id={}", id);
            throw new RuntimeException("Contact not found");
        }

        Contact contact = optionalContact.get();

        if (!contact.getUser().getEmail().equals(email)) {

            log.warn(
                    "Unauthorized update attempt. ContactId={}, User={}",
                    id,
                    email
            );

            throw new RuntimeException(
                    "You are not allowed to update this contact"
            );
        }

        log.info("Updating contact fields");

        contact.setName(updatedContact.getName());
        contact.setEmail(updatedContact.getEmail());
        contact.setPhone(updatedContact.getPhone());
        contact.setCompany(updatedContact.getCompany());
        contact.setJobTitle(updatedContact.getJobTitle());
        contact.setCity(updatedContact.getCity());

        // Duplicate detection
        log.info("Running AI duplicate detection for update");

        if (aiService.duplicate(contact)) {

            log.warn("Duplicate contact detected during update. id={}", id);

            throw new RuntimeException(
                    "Duplicate contact detected by AI"
            );
        }

        log.info("AI duplicate check passed");

        // Auto Tag
        String tags = aiService.autotag(
                contact.getName(),
                contact.getJobTitle(),
                contact.getCompany()
        );

        contact.setTags(tags);

        log.info("Generated tags={}", tags);

        // AI Insights
        String insights = aiService.generateInsights(contact);

        contact.setTags(contact.getTags() + ", " + insights);

        log.info("Generated insights={}", insights);

        Contact saved = contectRepo.save(contact);

        log.info("Contact updated successfully. id={}",
                saved.getId());

        return saved;
    }

    // ---------------------- Delete contact ----------------------
    public void delete(Long id) {

        log.info("Deleting contact id={}", id);

        if (!contectRepo.existsById(id)) {
            log.warn("Delete failed. Contact not found. id={}", id);
            return;
        }

        contectRepo.deleteById(id);

        log.info("Contact deleted successfully. id={}", id);
    }

    // ---------------------- Smart search ----------------------
    public List<Contact> search(String query, String email) {

        log.info("Smart search started. Query='{}', User={}",
                query,
                email);

        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {

            log.warn("Search failed. User not found={}",
                    email);

            return List.of();
        }

        List<Contact> contacts =
                contectRepo.findByUser(user);

        log.info("Found {} contacts for searching",
                contacts.size());

        String aiResult = aiService.smartSearch(query);

        log.info("AI Search Result={}", aiResult);

        List<Contact> result = contacts.stream()
                .filter(c -> {
                    String lowerAi = aiResult.toLowerCase();

                    return (c.getName() != null &&
                            lowerAi.contains(c.getName().toLowerCase()))
                            ||
                            (c.getCity() != null &&
                                    lowerAi.contains(c.getCity().toLowerCase()))
                            ||
                            (c.getCompany() != null &&
                                    lowerAi.contains(c.getCompany().toLowerCase()))
                            ||
                            (c.getJobTitle() != null &&
                                    lowerAi.contains(c.getJobTitle().toLowerCase()))
                            ||
                            (c.getTags() != null &&
                                    lowerAi.contains(c.getTags().toLowerCase()));
                })
                .toList();

        log.info("Search completed. {} contacts matched.",
                result.size());

        return result;
    }
}