package com.nellshark.springbootblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class UserLogInLogOutControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql("/save-user.sql")
    void should_authenticated_when_signIn() throws Exception {
        mockMvc.perform(formLogin("/sign-in")
                        .user("email", "user@gmail.com")
                        .password("password", "password123"))
                .andDo(print())
                .andExpect(authenticated());
    }


    @Test
    @WithMockUser
    void should_unAuthenticated_when_signOut() throws Exception {
        mockMvc.perform(logout("/sign-out"))
                .andDo(print())
                .andExpect(unauthenticated());
    }
}
