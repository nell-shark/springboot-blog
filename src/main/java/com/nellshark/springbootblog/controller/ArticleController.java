package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articles")
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/{title}")
    public String getArticle(@ModelAttribute Article article, Model model, @AuthenticationPrincipal User user) {
        if (user != null) model.addAttribute("user", user);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/create-new-article")
    @PreAuthorize("hasAuthority('CREATE_NEW_ARTICLES')")
    public String createNewArticle(Model model, @AuthenticationPrincipal User user) {
        if (user != null) model.addAttribute("user", user);
        return "create-new-article";
    }

    @PostMapping("/create-new-article")
    @PreAuthorize("hasAuthority('CREATE_NEW_ARTICLES')")
    public String postNewArticle(@RequestParam String title, @RequestParam String text) {
        Article article = new Article(title, text);
        articleService.saveArticle(article);
        return "redirect:/";
    }
}
