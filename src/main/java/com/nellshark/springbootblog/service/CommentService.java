package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.Comment;
import com.nellshark.springbootblog.model.User;
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
        log.info("Get all comments by Article " + article.toString());
        return commentRepository.findByArticle(article);
    }

    public List<Comment> getAllCommentsByUser(User user) {
        log.info("Get all comments by User " + user.toString());
        return commentRepository.findByUser(user);
    }

    public Comment saveComment(Comment comment) {
        log.info("Save the comment in db" + comment.toString());
        return commentRepository.save(comment);
    }
}
