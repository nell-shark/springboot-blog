package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.ArticleService;
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
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
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
class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Test
    void should_returnModelAndView_when_getIndexPage() throws Exception {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        when(articleService.getAllArticles()).thenReturn(List.of(article));

        MvcResult result = mockMvc.perform(get("/articles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("articles", hasItem(article)))
                .andReturn();

        assertEquals("articles/index", result.getModelAndView().getViewName());
    }

    @Test
    @WithMockUser(authorities = "CREATE_ARTICLES")
    void should_redirect_when_userWithAuthorityGetsArticleCreatePage() throws Exception {
        UUID uuid = UUID.randomUUID();
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);

        mockMvc.perform(get("/articles/create"))
                .andDo(print())
                .andExpect(redirectedUrl("/articles/create/" + uuid));
    }

    @Test
    @WithMockUser(authorities = "CREATE_ARTICLES")
    void should_returnModelAndView_when_userWithAuthorityGetsArticleCreatePage() throws Exception {
        UUID uuid = UUID.randomUUID();

        MvcResult result = mockMvc.perform(get("/articles/create/" + uuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("articles/create", result.getModelAndView().getViewName());
    }

    @Test
    void should_returnModelAndView_when_getArticleByIdPage() throws Exception {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();

        when(articleService.getArticleById(article.getId())).thenReturn(article);

        MvcResult result = mockMvc.perform(get("/articles/" + article.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("article", article))
                .andReturn();

        assertEquals("articles/id", result.getModelAndView().getViewName());
    }

    @Test
    @WithMockUser(authorities = "EDIT_ARTICLES")
    void should_returnModelAndView_when_userWithAuthorityGetsArticleEditPage() throws Exception {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        when(articleService.getArticleById(article.getId())).thenReturn(article);

        MvcResult result = mockMvc.perform(get("/articles/edit/" + article.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("article", article))
                .andReturn();

        assertEquals("articles/edit", result.getModelAndView().getViewName());
    }

    @Test
    @WithMockUser(authorities = "CREATE_ARTICLES")
    void should_redirect_when_userWithAuthorityCreatesArticle() throws Exception {
        Article article = Article.builder().id(UUID.randomUUID()).title("title").content("content").build();

        doNothing().when(articleService).save(any(Article.class), isNull());

        mockMvc.perform(post("/articles/" + article.getId())
                        .flashAttrs(Map.of("newArticle", article))
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/articles/" + article.getId()));
    }

    @Test
    @WithMockUser(authorities = "EDIT_ARTICLES")
    void should_redirect_when_userWithAuthorityUpdatesArticle() throws Exception {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();

        mockMvc.perform(patch("/articles/" + article.getId())
                        .flashAttrs(Map.of("article", article))
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/articles/" + article.getId()));
    }

    @Test
    @WithMockUser(authorities = "DELETE_ARTICLES")
    void should_redirect_when_userWithAuthorityDeletesArticleById() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(articleService).deleteArticleById(uuid);

        mockMvc.perform(delete("/articles/" + uuid)
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/articles"));
    }
}