package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.service.AppUserService;
import com.nellshark.springbootblog.service.ArticleService;
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
    private final AppUserService appUserService;

    @RequestMapping(value = {"/", "/main", "/home"}, method = RequestMethod.GET)
    public String main(Model model) {
        model.addAttribute("articles", articleService.findAllArticles());
        return "main";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("admins", appUserService.findAllAdmins());
        return "about";
    }
}
