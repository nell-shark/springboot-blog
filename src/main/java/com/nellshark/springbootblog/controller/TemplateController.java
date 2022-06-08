package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class TemplateController {
    @GetMapping("/")
    public String home(Model model){
        List<Article> strings = List.of(
                new Article("World", "Example", LocalDate.now(), "description"),
                new Article("War", "Example", LocalDate.now(), "description"),
                new Article("Piece", "Example", LocalDate.now(), "description")
        );
        model.addAttribute("articles", strings);
        return "home";
    }
}
