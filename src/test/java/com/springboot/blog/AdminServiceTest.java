package com.springboot.blog;

import static org.mockito.Mockito.lenient;

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
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        User admin = new User(1L, "Admin User", "admin", "admin@example.com", "password", Set.of(adminRole));

        lenient().when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        lenient().when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
    }

    @Test
    public void testPromoteUserToAdmin_Success() {
        // Arrange
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        User admin = new User(1L, "Admin User", "admin", "admin@example.com", "password", Set.of(adminRole));
        User userToPromote = new User(2L, "User", "user", "user@example.com", "password", new HashSet<>());

        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(userToPromote));

        // Act
        adminService.promoteUserToAdmin("admin", 2L);

        // Assert
        Assertions.assertTrue(userToPromote.getRoles().contains(adminRole));
        Mockito.verify(userRepository).save(userToPromote);
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
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        Role userRole = new Role(2L, "ROLE_USER");
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