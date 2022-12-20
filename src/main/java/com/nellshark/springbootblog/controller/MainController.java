package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.ArticleService;
import com.nellshark.springbootblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class MainController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping
    public String getIndexPage(@RequestParam(value = "search", required = false) String search,
                               Model model) {
        List<Article> articles = StringUtils.isEmpty(search)
                ? articleService.getAllArticles()
                : articleService.getArticleByContent(search);

        model.addAttribute("search", search);
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/contact-us")
    public String getContactUsPage(Model model) {
        List<User> admins = userService.getAllAdmins();
        List<User> moderators = userService.getAllModerators();

        model.addAttribute("authorities", ListUtils.union(admins, moderators));
        return "contact-us";
    }
}
