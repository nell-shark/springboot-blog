package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ArticleServiceTest {
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private FileService fileService;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository, fileService);
    }

    @Test
    void should_returnListOfArticles_when_getAllArticles() {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        List<Article> articleList = List.of(article);
        when(articleRepository.findAll()).thenReturn(articleList);

        List<Article> result = articleService.getAllArticles();

        assertEquals(articleList, result);
    }

    @Test
    void should_returnArticle_when_getArticleById() {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        when(articleRepository.findAll()).thenReturn(List.of(article));

        Article result = articleService.getArticleById(article.getId());

        assertEquals(article, result);
    }

    @Test
    void should_returnListOfArticles_when_doSearch() {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        List<Article> articleList = List.of(article);
        String search = "Search";
        when(articleRepository.doSearch(search)).thenReturn(articleList);

        List<Article> result = articleService.doSearch(search);

        assertEquals(articleList, result);
    }

    @Test
    void should_saveArticle() {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();

        articleService.save(article);
        ArgumentCaptor<Article> commentArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).save(commentArgumentCaptor.capture());

        assertEquals(article, commentArgumentCaptor.getValue());
    }

    @Test
    void should_deleteArticle_when_deleteArticleById() {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        List<Article> articleList = List.of(article);
        when(articleRepository.findAll()).thenReturn(articleList);

        articleService.deleteArticleById(article.getId());
        ArgumentCaptor<Article> commentArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).delete(commentArgumentCaptor.capture());

        assertEquals(article, commentArgumentCaptor.getValue());
    }
}