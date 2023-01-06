package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add/{articleId}")
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public String addComment(@PathVariable("articleId") UUID articleId,
                             @RequestParam("comment") String content,
                             @AuthenticationPrincipal User user) {
        commentService.saveComment(articleId, user, content);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("#user.getId().equals(authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    public String deleteComment(@PathVariable("id") Long id,
                                @AuthenticationPrincipal User user) {
        commentService.deleteCommentById(id);
        return "redirect:/";
    }
}
