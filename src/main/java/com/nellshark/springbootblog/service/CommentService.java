package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.Comment;
import com.nellshark.springbootblog.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getAllCommentsByArticle(Article article) {
        return commentRepository.findByArticle(article);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
