package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@SuppressWarnings("ConstantConditions")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void should_returnModelAndView_when_getContactUsPage() throws Exception {
        User user = User.builder().email("admin@gmail.com").password("password").build();

        when(userService.getBosses()).thenReturn(Set.of(user));

        MvcResult result = mockMvc.perform(get("/users/bosses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("bosses", hasItem(user)))
                .andReturn();

        assertEquals("users/bosses", result.getModelAndView().getViewName());
    }

    @Test
    void should_returnModelAndView_when_getSignUpPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("users/sign-up", result.getModelAndView().getViewName());
    }

    @Test
    void should_returnModelAndView_when_getSignInPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/sign-in"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("users/sign-in", result.getModelAndView().getViewName());
    }

    @Test
    void should_returnModelAndView_when_getUserByIdPage() throws Exception {
        Long randomLong = new Random().nextLong();
        User user = User.builder().id(randomLong).email("user@gmail.com").password("password").build();

        when(userService.getUserById(randomLong)).thenReturn(user);

        MvcResult result = mockMvc.perform(get("/users/" + randomLong))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("userById", user))
                .andReturn();

        assertEquals("users/id", result.getModelAndView().getViewName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_redirect_when_deleteUserById() throws Exception {
        long id = new Random().nextLong();
        doNothing().when(userService).deleteUserById(id);

        mockMvc.perform(delete("/users/" + id)
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void should_redirect_when_createNewUser() throws Exception {
        User user = User.builder().id(1L).email("test@gmail.com").password("password").build();
        doNothing().when(userService).save(user);
        doNothing().when(userService).authenticateUserAndSetSession(any(User.class), any(HttpServletRequest.class));

        mockMvc.perform(post("/users")
                        .flashAttrs(Map.of("newUser", user))
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/users/" + user.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_redirect_when_updateUser() throws Exception {
        User user = User.builder().id(1L).email("test@gmail.com").password("password").build();
        doNothing().when(userService).save(any(User.class), isNull());

        mockMvc.perform(patch("/users/" + user.getId())
                        .flashAttrs(Map.of("userById", user))
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/users/" + user.getId()));
    }
}