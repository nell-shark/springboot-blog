package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.Comment;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ArticleService articleService;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, articleService);
    }

    @Test
    void should_returnComment_when_getCommentById() {
        Comment comment = Comment.builder().id(1L).article(new Article()).user(new User()).content("content").build();
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        Comment result = commentService.getCommentById(comment.getId());

        assertEquals(comment, result);
    }

    @Test
    void should_saveComment() {
        Comment comment = Comment.builder().id(1L).article(new Article()).user(new User()).content("content").build();

        commentService.saveComment(comment);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        verify(commentRepository).save(commentArgumentCaptor.capture());

        assertEquals(comment, commentArgumentCaptor.getValue());
    }

    @Test
    void should_createAndSaveComment_when_articleIdFound() {
        Article article = Article.builder().id(UUID.randomUUID()).title("Title").content("Content").build();
        User user = User.builder().email("email@gmail.com").password("password").build();
        Comment comment = Comment.builder().article(article).user(user).content("content").build();

        when(articleService.getArticleById(article.getId())).thenReturn(article);

        commentService.saveComment(article.getId(), user, comment.getContent());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        verify(commentRepository).save(commentArgumentCaptor.capture());

        assertEquals(comment.getArticle(), commentArgumentCaptor.getValue().getArticle());
        assertEquals(comment.getUser(), commentArgumentCaptor.getValue().getUser());
        assertEquals(comment.getContent(), commentArgumentCaptor.getValue().getContent());
    }

    @Test
    void should_deleteCommentById_when_idFound() {
        Comment comment = Comment.builder().id(1L).article(new Article()).user(new User()).content("content").build();
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        commentService.deleteCommentById(comment.getId());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        verify(commentRepository).delete(commentArgumentCaptor.capture());

        assertEquals(comment, commentArgumentCaptor.getValue());
    }
}