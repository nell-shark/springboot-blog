package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("api/v1/create-article")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String postArticle(@RequestParam String title, @RequestParam String text) {
        Article article = new Article(title, text, LocalDate.now());
        articleService.save(article);
        return "redirect:/";
    }
}
