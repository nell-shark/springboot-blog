package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.ArticleNotFoundException;
import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        log.info("Find all articles");
        return articleRepository
                .findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public Article getById(Long id) {
        log.info("Find an article by id: " + id);
        return articleRepository
                .findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article with id = %s not found".formatted(id)));
    }

    public List<Article> searchArticle(String text) {
        log.info("Search article (%s)".formatted(text));
        return Stream.of(articleRepository.findByTitle(text), articleRepository.findByText(text))
                .flatMap(List::stream)
                .distinct()
                .toList();
    }

    public void saveArticle(Article article) {
        log.info("Save the article in db: " + article);
        articleRepository.save(article);
    }

    public void deleteAllArticles() {
        log.info("Delete all articles");
        articleRepository.deleteAll();
    }
}
