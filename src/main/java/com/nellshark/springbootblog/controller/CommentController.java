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
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public ModelAndView addComment(@RequestParam("articleId") UUID articleId,
                                   @RequestParam("content") String content,
                                   @AuthenticationPrincipal User user) {
        commentService.saveComment(articleId, user, content);
        return new ModelAndView("redirect:/articles/" + articleId);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "OR #user.getId().equals(authentication.principal.id)")
    public ModelAndView deleteCommentById(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal User user) {
        commentService.deleteCommentById(id);
        return new ModelAndView("redirect:/");
    }
}
