package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.CommentNotFoundException;
import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.Comment;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public Comment getCommentById(Long id) {
        log.info("Getting the comment by id: " + id);

        return commentRepository.findAll()
                .stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CommentNotFoundException("Comment with id=%s not found".formatted(id)));
    }

    public void saveComment(Comment comment) {
        log.info("Saving the comment in db:" + comment);
        commentRepository.save(comment);
    }

    public void saveComment(UUID articleId, User user, String content) {
        log.info("Creating the comment to save: " + content);

        Article article = articleService.getArticleById(articleId);

        Comment comment = Comment.builder()
                .article(article)
                .user(user)
                .content(content)
                .build();

        saveComment(comment);
    }

    public void deleteCommentById(Long id) {
        log.info("Deleting the comment by id: " + id);
        Comment comment = getCommentById(id);
        commentRepository.delete(comment);
    }
}
