package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT article " +
            "FROM Article article " +
            "WHERE LOWER(article.title) LIKE LOWER(CONCAT('%',:title, '%')) ")
    List<Article> findByTitle(String title);

    @Query("SELECT article " +
            "FROM Article article " +
            "WHERE LOWER(article.text) LIKE LOWER(CONCAT('%',:text, '%')) ")
    List<Article> findByText(String text);
}
