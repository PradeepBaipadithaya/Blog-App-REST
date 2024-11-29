package com.springboot.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Promotes a user to the ADMIN role.
     *
     * @param adminUsername the username of the current admin making the request.
     * @param userId        the ID of the user to be promoted.
     */
    public void promoteUserToAdmin(String adminUsername, Long userId) {
        // Ensure the requesting user is an admin
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found."));
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("Only admins can promote users.");
        }

        // Find the user to promote
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Find the ADMIN role
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Admin role not found."));

        // Add the ADMIN role to the user if not already assigned
        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User is already an admin.");
        }
    }

    /**
     * Lists all users except those with the ADMIN role.
     *
     * @return List of non-admin users.
     */
    public List<User> listNonAdminUsers() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Admin role not found."));
        return userRepository.findAll().stream()
                .filter(user -> !user.getRoles().contains(adminRole))
                .toList();
    }

    /**
     * Checks if a user is an admin.
     *
     * @param user the user to check.
     * @return true if the user has the ADMIN role, false otherwise.
     */
    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
    }
}
