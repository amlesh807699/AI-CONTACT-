package com.example.demo.Ai;

import com.example.demo.Entity.Contact;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AiService {

    private final Client geminiClient;

    public AiService(@Value("${gemini.api.key}") String apiKey) {
        // Spring Boot property se system property set karo
        System.setProperty("GOOGLE_API_KEY", apiKey);

        // Ab Google client ko key mil gayi
        this.geminiClient = new Client();
    }

    public Client getGeminiClient() {
        return geminiClient;
    }

    private String generate(String prompt) {
        // ✅ Use geminiClient.models WITHOUT ()
        GenerateContentResponse response = geminiClient.models
                .generateContent("gemini-2.5-flash", prompt, null);

        return response.text().trim();
    }

    public String autotag(String name, String jobTitle, String company) {
        String prompt = String.format(
                "Suggest 3 short tags for a person named %s who works as %s at %s. Reply only comma separated.",
                name, jobTitle, company
        );
        return generate(prompt);
    }

    public boolean duplicate(Contact contact) {
        String prompt = String.format(
                "Check if this contact is a duplicate. Reply only Yes or No.\n" +
                        "Name: %s, Email: %s, Phone: %s, Company: %s, JobTitle: %s, City: %s",
                contact.getName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getCompany(),
                contact.getJobTitle(),
                contact.getCity()
        );
        return generate(prompt).equalsIgnoreCase("Yes");
    }

    public String generateInsights(Contact contact) {
        String prompt = String.format(
                "Give a short follow-up insight for this contact:\n" +
                        "Name: %s, Company: %s, Job: %s, Tags: %s",
                contact.getName(),
                contact.getCompany(),
                contact.getJobTitle(),
                contact.getTags()
        );
        return generate(prompt);
    }

    public String smartSearch(String query) {
        String prompt =
                "Interpret this contact search query and return JSON filters.\n" +
                        "Possible fields: name, city, company, jobTitle, tags.\n" +
                        "Query: \"" + query + "\"";
        return generate(prompt);
    }

    public String smartuser(String query) {
        String prompt =
                "Interpret this user search query and return JSON filters.\n" +
                        "Possible fields: name, email, role, verfied.\n" +
                        "Query: \"" + query + "\"";
        return generate(prompt);
    }




}




