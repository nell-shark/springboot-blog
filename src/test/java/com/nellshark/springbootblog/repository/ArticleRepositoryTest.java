package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@TestPropertySource("/application-test.properties")
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void testFindArticleByTitle() {
        String title = "Some title";
        Article article = new Article(title, "");
        underTest.save(article);
        List<Article> articles = underTest.findByTitle(title.split(" ")[1].toUpperCase());

        assertFalse(CollectionUtils.isEmpty(articles));
        assertEquals(title, articles.get(0).getTitle());
    }

    @Test
    void testFindArticleByText() {
        String text = "Some text";
        Article article = new Article("", text);
        underTest.save(article);
        List<Article> articles = underTest.findByText(text.split(" ")[1].toUpperCase());

        assertFalse(CollectionUtils.isEmpty(articles));
        assertEquals(text, articles.get(0).getText());
    }
}