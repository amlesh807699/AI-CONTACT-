package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Phone number is required")
    @Digits(integer = 10, fraction = 0, message = "Phone number must be 10 digits")
    private Long phone;

    @NotBlank(message = "Company cannot be empty")
    private String company;

    @NotBlank(message = "Job title cannot be empty")
    private String jobTitle;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @Size(max = 100, message = "Tags can be max 100 characters")
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    @JsonIgnore   // ⭐⭐ THIS LINE FIXES YOUR ERROR
    private User user;
}
