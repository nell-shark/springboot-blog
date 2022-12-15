package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute
    public void addUserToModel(Model model,
                               @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
    }
}
