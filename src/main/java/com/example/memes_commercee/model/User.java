package com.example.memes_commercee.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    public String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    public String email;

    @Column(name = "birthday")
    public LocalDate birthday;

    @Column(name = "location", length = 255)
    public String location;

    @Column(name = "aadhar", unique = true, length = 12)
    public String aadhar;

    @Column(name = "phone", length = 15)
    public String phone;

    @Column(name = "date_of_joining")
    public LocalDateTime dateOfJoining;

    @Column(name = "nickname", length = 50)
    public String nickname;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    // Default constructor required by JPA
    public User() {}

    // Constructor for creating new users
    public User(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateOfJoining = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dateOfJoining == null) {
            dateOfJoining = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
