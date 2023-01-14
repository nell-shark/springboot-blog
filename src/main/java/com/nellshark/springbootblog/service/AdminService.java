package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ArticleService articleService;
    private final UserService userService;

    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
