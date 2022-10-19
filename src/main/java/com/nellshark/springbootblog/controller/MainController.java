package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.service.ArticleService;
import com.nellshark.springbootblog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
@AllArgsConstructor
public class MainController {
    private final ArticleService articleService;
    private final UserService appUserService;

    @RequestMapping(value = {"/", "/main", "/home"}, method = RequestMethod.GET)
    public String main(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "main";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("admins", appUserService.getAllAdmins());
        return "about";
    }
}
