package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("users")
    public ModelAndView getUsers() {
        return new ModelAndView("admin/users")
                .addObject("users", adminService.getAllUsers());
    }

    @GetMapping("articles")
    public ModelAndView getArticles(Model model) {
        return new ModelAndView("admin/articles")
                .addObject("articles", adminService.getAllArticles());
    }
}
