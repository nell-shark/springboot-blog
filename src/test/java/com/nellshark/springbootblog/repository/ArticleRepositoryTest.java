package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @CsvSource({
            "Search By Title, search",
            "Search By Title, by",
            "Search By Title, title",
            "search by title, SEARCH BY TITLE",
            "SEARCH BY TITLE, search by title"
    })
    void testSearchArticlesByTitle(String title, String search) {
        Article article = new Article(title, "");
        underTest.save(article);

        List<Article> searchByTitle = underTest.search(search);

        assertFalse(CollectionUtils.isEmpty(searchByTitle));
        assertEquals(title, searchByTitle.get(0).getTitle());
    }

    @ParameterizedTest
    @CsvSource({
            "Search By Text, search",
            "Search By Text, by",
            "Search By Text, text",
            "search by text, SEARCH BY TEXT",
            "SEARCH BY TEXT, search by text"
    })
    void testSearchArticlesByText(String text, String search) {
        Article article = new Article("", text);
        underTest.save(article);

        List<Article> searchByText = underTest.search(search);

        assertFalse(CollectionUtils.isEmpty(searchByText));
        assertEquals(text, searchByText.get(0).getText());
    }
}