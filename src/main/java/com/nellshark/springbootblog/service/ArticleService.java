package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.ArticleNotFoundException;
import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        log.info("Get all articles");
        return articleRepository
                .findAll(Sort.by(Sort.Direction.DESC, "published"));
    }

    public Article getArticleById(Long id) {
        log.info("Get an article by id: " + id);
        return articleRepository
                .findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article with id='%s' not found".formatted(id)));
    }

    public Article getArticleByTitle(String title) {
        log.info("Get an article by title: " + title);
        return articleRepository
                .findByTitle(title)
                .orElseThrow(() -> new ArticleNotFoundException("Article with title='%s' not found".formatted(title)));
    }

    public Article getArticleByLink(String link) {
        log.info("Get an article by link: " + link);
        return articleRepository.findAll()
                .stream()
                .filter(article -> article.getLink().equals(link))
                .findFirst()
                .orElseThrow(() -> new ArticleNotFoundException("Article with link='%s' not found".formatted(link)));
    }

    public List<Article> searchArticle(String search) {
        log.info("Search an article (%s)".formatted(search));
        return articleRepository.search(search);
    }

    public void saveArticle(Article article) {
        log.info("Save the article in db: " + article);
        if (article.getPublished() == null) {
            article.setPublished(LocalDate.now());
        }
        articleRepository.save(article);
    }

    public Long getNextSeriesId() {
        log.info("Get id of the new article");
        return articleRepository.getMaxId() + 1L;
    }

    public void deleteAllArticles() {
        log.info("Delete all articles");
        articleRepository.deleteAll();
    }
}
