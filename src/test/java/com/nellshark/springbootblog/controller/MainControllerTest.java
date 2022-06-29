package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.AppUser;
import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.service.AppUserService;
import com.nellshark.springbootblog.service.ArticleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private AppUserService appUserService;

    @AfterEach
    void tearDown() {
        articleService.deleteAll();
    }

    @Test
    void checkMainPageHasArticle() throws Exception {
        String title = "title";
        LocalDate date = LocalDate.now();
        Article article = new Article(title, "text", date);
        when(articleService.findAllArticles()).thenReturn(List.of(article));

        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(title)))
                .andExpect(content().string(containsString(date.toString())));
    }

    @Test
    void checkAboutPageHasAdmin() throws Exception {
        String email = "test@gmail.com";
        AppUser user = new AppUser(email, "password", UserRole.ADMIN);
        when(appUserService.findAllAdmins()).thenReturn(List.of(user));

        this.mockMvc.perform(get("/about"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(email)));
    }
}
