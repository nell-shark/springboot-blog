package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    @Query("SELECT article " +
            "FROM Article article " +
            "WHERE LOWER(article.title) LIKE LOWER(CONCAT('%',:search, '%')) " +
            "OR LOWER(article.content) LIKE LOWER(CONCAT('%',:search, '%')) ")
    List<Article> search(String search);
}
