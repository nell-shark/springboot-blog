package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final ArticleService articleService;
    private final UserService userService;

    public List<Article> getAllArticles() {
        log.info("Getting all articles by admin");
        return articleService.getAllArticles();
    }

    public List<User> getAllUsers() {
        log.info("Getting all users by admin");
        return userService.getAllUsers();
    }
}
