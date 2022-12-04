package com.nellshark.springbootblog.repository;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT comment " +
            "FROM Comment comment " +
            "WHERE comment.article = :article " +
            "ORDER BY comment.id DESC")
    List<Comment> findByArticle(Article article);
}
