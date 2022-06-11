package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> getAllArticles(){
        log.info("get all articles");
        return articleRepository.findAll();
    }
}
