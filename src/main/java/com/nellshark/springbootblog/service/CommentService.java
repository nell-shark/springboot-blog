package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.Comment;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    public void saveComment(Comment comment) {
        log.info("Saving the comment in db:" + comment);
        commentRepository.save(comment);
    }

    public void saveComment(Article article, User user, String content) {
        log.info("Creating the comment to save: " + content);

        Comment comment = Comment.builder()
                .article(article)
                .user(user)
                .content(content)
                .build();

        saveComment(comment);
    }
}
