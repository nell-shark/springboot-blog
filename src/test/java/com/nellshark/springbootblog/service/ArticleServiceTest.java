package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.repository.ArticleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;


@SpringBootTest
class ArticleServiceTest {
    @Autowired
    private ArticleService underTest;

    @MockBean
    private ArticleRepository articleRepository;

    @Test
    void testSearchArticleByTitleAndText() {
        Article article = new Article("abc", "abc");
        when(articleRepository.findByTitle("abc")).thenReturn(List.of(article));
        when(articleRepository.findByText("abc")).thenReturn(List.of(article));

        Assertions.assertEquals(List.of(article), underTest.searchArticle("abc"));
    }
}