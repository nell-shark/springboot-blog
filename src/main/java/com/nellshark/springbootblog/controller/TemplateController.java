package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.AppUserService;
import com.nellshark.springbootblog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@AllArgsConstructor
public class TemplateController {
    private final ArticleService articleService;
    private final AppUserService appUserService;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("articles", articleService.findAllArticles());
        return "main";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("admins", appUserService.findAllAdmins());
        return "about";
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @GetMapping("/articles/{id}")
    public String article(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("api/v1/create-article")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String createArticle() {
        return "create-article";
    }
}
