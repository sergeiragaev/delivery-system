package ru.skillbox.authservice.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.authservice.domain.User;
import ru.skillbox.authservice.repository.UserRepository;
import ru.skillbox.authservice.security.SecurityConfiguration;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
class UserControllerNotAuthenticatedTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Configuration
    @ComponentScan(basePackageClasses = {UserController.class, SecurityConfiguration.class})
    public static class TestConf {
    }

    @Test
    void getUser() throws Exception {
        mvc.perform(get("/user/Petrov"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllUsers() throws Exception {
        mvc.perform(get("/user/"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUser() throws Exception {
        Mockito.when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0) + "_some_fake_encoding");
        User newUser = new User(
                "Ivanov",
                passwordEncoder.encode("superpass99")
        );
        Mockito.when(userRepository.save(refEq(newUser))).thenReturn(newUser);
        mvc.perform(
                post("/user/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ivanov\",\"password\":\"superpass99\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(newUser.getName())));
    }
}
