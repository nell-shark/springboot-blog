package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT article " +
            "FROM Article article " +
            "WHERE article.title = :title")
    Optional<Article> findByTitle(String title);

    @Query("SELECT article " +
            "FROM Article article " +
            "WHERE LOWER(article.title) LIKE LOWER(CONCAT('%',:search, '%')) " +
            "OR LOWER(article.text) LIKE LOWER(CONCAT('%',:search, '%')) ")
    List<Article> search(String search);

    @Query("SELECT coalesce(max(id), 0) FROM Article article")
    Long getMaxId();
}
