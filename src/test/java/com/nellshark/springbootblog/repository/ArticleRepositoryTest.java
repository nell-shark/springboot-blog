package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import org.apache.commons.lang3.StringUtils;
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
    @CsvSource(value = {
            "Search By Title, null, search",
            "Search By Title, null ,by",
            "Search By Title, null ,title",
            "search by title, null ,SEARCH BY TITLE",
            "SEARCH BY TITLE, null ,search by title",
            "null, Search By Text, search",
            "null, Search By Text, by",
            "null, Search By Text, text",
            "null, search by text, SEARCH BY TEXT",
            "null, SEARCH BY TEXT, search by text"
    }, nullValues = {"null"})
    void testSearchArticles(String title, String text, String search) {
        System.out.println(StringUtils.isEmpty(text));
        Article article = new Article(title, null, text);
        underTest.save(article);

        List<Article> searchByText = underTest.search(search);

        assertFalse(CollectionUtils.isEmpty(searchByText));

        assertEquals(title, searchByText.get(0).getTitle());
        assertEquals(text, searchByText.get(0).getText());
    }
}