package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AdminServiceTest {
    @MockBean
    private ArticleService articleService;
    @MockBean
    private UserService userService;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(articleService, userService);
    }

    @Test
    void should_returnListOfArticles_when_GetAllArticles() {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        List<Article> articleList = List.of(article);
        when(articleService.getAllArticles()).thenReturn(articleList);

        assertEquals(articleList, adminService.getAllArticles());
    }

    @Test
    void should_returnListOfUsers_when_GetAllUsers() {
        User user = User.builder().email("email@gmail.com").password("password").build();
        List<User> userList = List.of(user);
        when(userService.getAllUsers()).thenReturn(userList);

        assertEquals(userList, adminService.getAllUsers());
    }
}