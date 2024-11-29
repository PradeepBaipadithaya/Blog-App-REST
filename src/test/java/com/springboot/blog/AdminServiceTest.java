package com.springboot.blog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.AdminService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        Role adminRole = new Role(1L, "ROLE_ADMIN", null);
        User admin = new User(1L, "Admin User", "admin", "admin@example.com", "password", Set.of(adminRole));

        lenient().when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        lenient().when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
    }

    @Test
    void testPromoteUserToAdmin_Success() {
        // Arrange
        User adminUser = new User(1L, "Admin", "adminUser", "admin@example.com", "password",
                new HashSet<>(Set.of(new Role(1L, "ROLE_ADMIN", null))));
        User targetUser = new User(2L, "Target", "targetUser", "target@example.com", "password",
                new HashSet<>(Set.of(new Role(2L, "ROLE_USER", null))));

        Role adminRole = new Role(1L, "ROLE_ADMIN", null);

        // Mock repository behavior
        when(userRepository.findByUsername("adminUser")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));

        // Act
        adminService.promoteUserToAdmin("adminUser", 2L);

        // Assert
        assertTrue(targetUser.getRoles().contains(adminRole));
    }

    @Test
    public void testPromoteUserToAdmin_UserNotFound() {
        // Arrange
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> adminService.promoteUserToAdmin("admin", 2L));

        // No unnecessary stubbings here
    }

    @Test
    public void testListNonAdminUsers() {
        // Arrange
        Role adminRole = new Role(1L, "ROLE_ADMIN", null);
        Role userRole = new Role(2L, "ROLE_USER", null);
        User admin = new User(1L, "Admin User", "admin", "admin@example.com", "password", Set.of(adminRole));
        User user = new User(2L, "User", "user", "user@example.com", "password", Set.of(userRole));

        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        Mockito.when(userRepository.findAll()).thenReturn(List.of(admin, user));

        // Act
        List<User> nonAdmins = adminService.listNonAdminUsers();

        // Assert
        Assertions.assertEquals(1, nonAdmins.size());
        Assertions.assertEquals("user", nonAdmins.get(0).getUsername());
    }
}