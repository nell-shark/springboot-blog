package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping
@AllArgsConstructor
public class TemplateController {
    private final ArticleService articleService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }
}
