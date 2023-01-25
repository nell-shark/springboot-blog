package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser(authorities = "WRITE_COMMENTS")
    void should_redirect_whenUserWithAuthorityAddsComment() throws Exception {
        UUID articleId = UUID.randomUUID();

        doNothing().when(commentService).saveComment(eq(articleId), any(User.class), anyString());

        mockMvc.perform(post("/comments")
                        .param("articleId", articleId.toString())
                        .param("content", "text")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/articles/" + articleId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_redirect_whenAdminDeletesCommentById() throws Exception {
        long commentId = new Random().nextLong();

        doNothing().when(commentService).deleteCommentById(commentId);

        mockMvc.perform(delete("/comments/" + commentId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/"));
    }
}