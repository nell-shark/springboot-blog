package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@SuppressWarnings("ConstantConditions")
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_returnModelAndView_when_adminGetsUsers() throws Exception {
        User user = User.builder().email("user@gmail.com").password("password").build();

        when(adminService.getAllUsers()).thenReturn(List.of(user));

        MvcResult result = mockMvc.perform(get("/admin/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", hasItem(user)))
                .andReturn();

        assertEquals("admin/users", result.getModelAndView().getViewName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_returnModelAndView_when_adminGetsArticles() throws Exception {
        Article article = Article.builder().id(UUID.randomUUID()).title("title").content("content").build();

        when(adminService.getAllArticles()).thenReturn(List.of(article));

        MvcResult result = mockMvc.perform(get("/admin/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("articles", hasItem(article)))
                .andReturn();

        assertEquals("admin/articles", result.getModelAndView().getViewName());
    }
}