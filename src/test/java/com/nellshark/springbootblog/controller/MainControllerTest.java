package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.model.UserRole;
import com.nellshark.springbootblog.service.ArticleService;
import com.nellshark.springbootblog.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private UserService appUserService;

    @AfterEach
    void tearDown() {
        articleService.deleteAllArticles();
    }

    @Test
    void checkMainPageHasArticle() throws Exception {
        final String title = "title";
        Article article = Article.builder()
                .id(UUID.randomUUID())
                .title(title)
                .content("")
                .build();
        given(articleService.getAllArticles()).willReturn(List.of(article));

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(title)));
    }

    @Test
    void checkContactUsPageHasAdminAndModerator() throws Exception {
        String adminEmail = "admin@gmail.com";
        User admin = User.builder()
                .email(adminEmail)
                .password("")
                .role(UserRole.ROLE_ADMIN)
                .build();

        String moderatorEmail = "admin@gmail.com";
        User moderator = User.builder()
                .email(adminEmail)
                .password("")
                .role(UserRole.ROLE_ADMIN)
                .build();

        given(appUserService.getAdminsAndModerators()).willReturn(Set.of(admin, moderator));

        mockMvc.perform(get("/contact-us"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(adminEmail)))
                .andExpect(content().string(containsString(moderatorEmail)));
    }

    @Test
    void testDoSearchArticles() throws Exception {
        String title = "title";
        Article article = Article.builder()
                .id(UUID.randomUUID())
                .title(title)
                .content("")
                .build();

        given(articleService.doSearch(title)).willReturn(List.of(article));

        mockMvc.perform(get("/").param("search", title))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(title)));
    }
}
