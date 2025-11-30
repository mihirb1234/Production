package com.example.memes_commercee.repository;

import com.example.memes_commercee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByAadhar(String aadhar);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByAadhar(String aadhar);

    boolean existsByPhone(String phone);
}
