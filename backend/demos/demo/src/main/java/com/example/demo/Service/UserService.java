package com.example.demo.Service;

import com.example.demo.Ai.AiService;
import com.example.demo.Entity.Contact;import com.example.demo.Entity.Contact;
import com.example.demo.Entity.User;
import com.example.demo.Repo.ContectRepo;
import com.example.demo.Repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ContectRepo contectRepo;
    private final AiService aiService; // Inject your AI service
    public UserRepo getUserRepo() {
        return userRepo;
    }

    public ContectRepo getContectRepo() {
        return contectRepo;
    }

    public AiService getAiService() {
        return aiService;
    }


    // ---------------------- Get all contacts for a user ----------------------
    public List<Contact> getContect(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return List.of();
        return contectRepo.findByUser(user);
    }

    // ---------------------- Get contact by ID ----------------------
    public Contact getContactById(Long id, String email) {
        Contact contact = contectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        if (!contact.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        return contact;
    }

    // ---------------------- Get User by Email ----------------------
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    // ---------------------- Add new contact with AI ----------------------
    public Contact add(Contact contect) {
        // 1️⃣ Duplicate check
        boolean isDuplicate = aiService.duplicate(contect);
        if (isDuplicate) {
            throw new RuntimeException("Duplicate contact detected by AI");
        }

        // 2️⃣ Auto-tagging
        String tags = aiService.autotag(contect.getName(), contect.getJobTitle(), contect.getCompany());
        contect.setTags(tags);

        // 3️⃣ Save to DB
        Contact savedContact = contectRepo.save(contect);

        // 4️⃣ AI Insights & Reminders
        String insights = aiService.generateInsights(savedContact);
        savedContact.setTags(savedContact.getTags() + ", " + insights); // or store insights separately

        return savedContact;
    }

    // ---------------------- Update contact with AI ----------------------
    public Contact updateContact(Long id, Contact updatedContact, String email) {
        Optional<Contact> optionalContect = contectRepo.findById(id);
        if (optionalContect.isEmpty()) throw new RuntimeException("Contact not found");

        Contact contact = optionalContect.get();

        // Ensure the contact belongs to the logged-in user
        if (!contact.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to update this contact");
        }

        // Update fields
        contact.setName(updatedContact.getName());
        contact.setEmail(updatedContact.getEmail());
        contact.setPhone(updatedContact.getPhone());
        contact.setCompany(updatedContact.getCompany());
        contact.setJobTitle(updatedContact.getJobTitle());
        contact.setCity(updatedContact.getCity());

        // 1️⃣ Duplicate detection
        if (aiService.duplicate(contact)) {
            throw new RuntimeException("Duplicate contact detected by AI");
        }

        // 2️⃣ Auto-tagging
        String tags = aiService.autotag(contact.getName(), contact.getJobTitle(), contact.getCompany());
        contact.setTags(tags);

        // 3️⃣ AI Insights & Reminders
        String insights = aiService.generateInsights(contact);
        contact.setTags(contact.getTags() + ", " + insights); // optional append insights

        return contectRepo.save(contact);
    }

    // ---------------------- Delete contact ----------------------
    public void delete(Long id) {
        contectRepo.deleteById(id);
    }

    // ---------------------- Smart search (natural language) ----------------------
    public List<Contact> search(String query, String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return List.of();

        // 1️⃣ Get all user's contacts
        List<Contact> contacts = contectRepo.findByUser(user);

        // 2️⃣ Get AI JSON result
        String aiResult = aiService.smartSearch(query);

        // 3️⃣ Parse AI result to filter contacts
        // Assume AI returns JSON like: {"name":"John","city":"Delhi","company":"XYZ"}
        // For simplicity, let's filter manually
        return contacts.stream()
                .filter(c -> {
                    String lowerAi = aiResult.toLowerCase();
                    return (c.getName() != null && lowerAi.contains(c.getName().toLowerCase())) ||
                            (c.getCity() != null && lowerAi.contains(c.getCity().toLowerCase())) ||
                            (c.getCompany() != null && lowerAi.contains(c.getCompany().toLowerCase())) ||
                            (c.getJobTitle() != null && lowerAi.contains(c.getJobTitle().toLowerCase())) ||
                            (c.getTags() != null && lowerAi.contains(c.getTags().toLowerCase()));
                })
                .toList();
    }

}
