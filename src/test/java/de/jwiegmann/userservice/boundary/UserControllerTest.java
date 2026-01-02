package de.jwiegmann.userservice.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jwiegmann.userservice.control.UserService;
import de.jwiegmann.userservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User testUser;
    private UserDTO testDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        testDTO = new UserDTO();
        testDTO.setId(1L);
        testDTO.setEmail("test@example.com");
        testDTO.setFirstName("Test");
        testDTO.setLastName("User");
    }

    @Nested
    class CreateUser {
        @Test
        void shouldCreateUser_whenValidRequest() throws Exception {
            when(userMapper.toEntity(any(UserDTO.class))).thenReturn(testUser);
            when(userService.create(any(User.class))).thenReturn(testUser);
            when(userMapper.toDto(any(User.class))).thenReturn(testDTO);

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("test@example.com"));
        }

        @Test
        void shouldReturnBadRequest_whenInvalidEmail() throws Exception {
            testDTO.setEmail("invalid-email");

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequest_whenMissingFirstName() throws Exception {
            testDTO.setFirstName("");

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldReturnUser_whenUserExists() throws Exception {
            when(userService.findById(1L)).thenReturn(Optional.of(testUser));
            when(userMapper.toDto(testUser)).thenReturn(testDTO);

            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("test@example.com"));
        }

        @Test
        void shouldReturnNotFound_whenUserNotExists() throws Exception {
            when(userService.findById(99L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/users/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class FindAll {
        @Test
        void shouldReturnAllUsers() throws Exception {
            when(userService.findAll()).thenReturn(List.of(testUser));
            when(userMapper.toDto(testUser)).thenReturn(testDTO);

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].email").value("test@example.com"));
        }
    }
}
