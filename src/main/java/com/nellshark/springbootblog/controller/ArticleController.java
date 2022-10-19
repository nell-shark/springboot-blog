package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/articles")
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = articleService.getById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/create-new-article")
    @PreAuthorize("hasAuthority('CREATE_NEW_ARTICLES')")
    public String createNewArticle() {
        return "create-new-article";
    }

    @PostMapping("/create-new-article")
    @PreAuthorize("hasAuthority('CREATE_NEW_ARTICLES')")
    public String postNewArticle(@RequestParam String title, @RequestParam String text) {
        Article article = new Article(title, text, LocalDate.now());
        articleService.saveArticle(article);
        return "redirect:/";
    }
}
