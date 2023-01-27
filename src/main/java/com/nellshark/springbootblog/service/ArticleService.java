package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.ArticleNotFoundException;
import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final FileService fileService;

    public List<Article> getAllArticles() {
        log.info("Getting all articles");
        return articleRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Article::getLocalDateTime).reversed())
                .toList();
    }

    public Article getArticleById(UUID id) {
        log.info("Getting the article by id: " + id);
        return articleRepository.findAll()
                .stream()
                .filter(article -> article.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ArticleNotFoundException("Article with id='%s' not found".formatted(id)));
    }

    public List<Article> doSearch(String search) {
        log.info("Searching an article: " + search);
        return articleRepository.doSearch(search);
    }

    public void save(Article article) {
        log.info("Saving the article in db: " + article);
        articleRepository.save(article);
    }

    public void save(Article article, MultipartFile thumbnail) throws IOException {
        log.info("Saving the article with thumbnail in db: " + article);
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String fileFolder = "/articles/" + article.getId();
            String _thumbnail = fileService.saveMultipartFileToLocalStorage(thumbnail, fileFolder);
            article.setThumbnail(_thumbnail);
        }
        articleRepository.save(article);
    }

    public void deleteArticleById(UUID id) {
        log.info("Deleting the article by id: " + id);
        Article article = getArticleById(id);
        articleRepository.delete(article);
    }
}
