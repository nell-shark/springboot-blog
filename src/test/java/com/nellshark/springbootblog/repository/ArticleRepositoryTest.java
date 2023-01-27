package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Title", "content"})
    void should_returnListOfArticles_when_doSearch(String search) {
        Article article = Article.builder().id(UUID.randomUUID()).title("This is a title").content("This is a content").build();
        articleRepository.save(article);

        List<Article> result = articleRepository.doSearch(search);

        assertEquals(List.of(article), result);
    }
}