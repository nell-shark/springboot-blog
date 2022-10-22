package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.ArticleService;
import com.nellshark.springbootblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class MainController {
    private final ArticleService articleService;
    private final UserService appUserService;

    @RequestMapping(value = {"/", "/index", "/main", "/home"}, method = RequestMethod.GET)
    public String index(Model model, @Param("search") String search) {
        List<Article> articles = StringUtils.isEmpty(search)
                ? articleService.getAllArticles()
                : articleService.searchArticle(search);

        model.addAttribute("articles", articles);
        return "index";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Model model) {
        model.addAttribute("admins", appUserService.getAllAdmins());
        return "about";
    }
}
