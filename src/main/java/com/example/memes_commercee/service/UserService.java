package com.example.memes_commercee.service;

import com.example.memes_commercee.model.User;
import com.example.memes_commercee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByAadhar(String aadhar) {
        return userRepository.findByAadhar(aadhar);
    }

    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User createUser(User user) {
        // Additional validation can be added here
        if (userRepository.existsByEmail(user.email)) {
            throw new RuntimeException("User with this email already exists");
        }
        if (user.aadhar != null && userRepository.existsByAadhar(user.aadhar)) {
            throw new RuntimeException("User with this Aadhar already exists");
        }
        if (user.phone != null && userRepository.existsByPhone(user.phone)) {
            throw new RuntimeException("User with this phone number already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Check for duplicate email if email is being changed
            if (!existingUser.email.equals(updatedUser.email) &&
                userRepository.existsByEmail(updatedUser.email)) {
                throw new RuntimeException("Email already exists");
            }

            // Check for duplicate aadhar if aadhar is being changed
            if (updatedUser.aadhar != null &&
                !updatedUser.aadhar.equals(existingUser.aadhar) &&
                userRepository.existsByAadhar(updatedUser.aadhar)) {
                throw new RuntimeException("Aadhar already exists");
            }

            // Check for duplicate phone if phone is being changed
            if (updatedUser.phone != null &&
                !updatedUser.phone.equals(existingUser.phone) &&
                userRepository.existsByPhone(updatedUser.phone)) {
                throw new RuntimeException("Phone number already exists");
            }

            existingUser.fullName = updatedUser.fullName;
            existingUser.email = updatedUser.email;
            existingUser.birthday = updatedUser.birthday;
            existingUser.location = updatedUser.location;
            existingUser.aadhar = updatedUser.aadhar;
            existingUser.phone = updatedUser.phone;
            existingUser.nickname = updatedUser.nickname;

            return userRepository.save(existingUser);
        }
        throw new RuntimeException("User not found");
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
