package de.jwiegmann.userservice.control;

import de.jwiegmann.userservice.entity.User;
import de.jwiegmann.userservice.entity.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
    }

    @Nested
    class Create {
        @Test
        void shouldCreateUser_whenValidUser() {
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            User result = userService.create(testUser);

            assertThat(result.getEmail()).isEqualTo("test@example.com");
            verify(userRepository).save(testUser);
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldReturnUser_whenUserExists() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            Optional<User> result = userService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        }

        @Test
        void shouldReturnEmpty_whenUserNotExists() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<User> result = userService.findById(99L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class FindAll {
        @Test
        void shouldReturnAllUsers() {
            when(userRepository.findAll()).thenReturn(List.of(testUser));

            List<User> result = userService.findAll();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdateUser_whenUserExists() {
            User updateData = new User();
            updateData.setEmail("updated@example.com");
            updateData.setFirstName("Updated");
            updateData.setLastName("Name");

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            User result = userService.update(1L, updateData);

            assertThat(result).isNotNull();
            verify(userRepository).save(testUser);
        }

        @Test
        void shouldThrowException_whenUserNotExists() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.update(99L, testUser))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User not found");
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteUser() {
            doNothing().when(userRepository).deleteById(1L);

            userService.delete(1L);

            verify(userRepository).deleteById(1L);
        }
    }
}
