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

import static com.nellshark.springbootblog.service.FileService.APP_LOCATION;
import static com.nellshark.springbootblog.service.FileService.STORAGE_FOLDER;

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
        log.info("Getting an article by id: " + id);
        return articleRepository.findAll()
                .stream()
                .filter(article -> article.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ArticleNotFoundException("Article with id='%s' not found".formatted(id)));
    }

    public List<Article> doSearch(String search) {
        log.info("Searching an article: " + search);
        return articleRepository.search(search);
    }

    public Article saveArticle(Article article) {
        log.info("Saving the article in db: " + article);
        return articleRepository.save(article);
    }

    public Article saveArticle(UUID id, String title, String content, MultipartFile file) throws IOException {
        log.info("Creating or Updating an article to save: " + id);
        Article article = Article.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();

        if (file != null && !file.isEmpty()) {
            String image = saveArticleImage(file, id);
            article.setThumbnail(image);
        }

        return saveArticle(article);
    }


    public String saveArticleImage(MultipartFile file, UUID id) throws IOException {
        log.info("Saving the Article's Image");

        String newFileName = fileService.getNewFileName(file.getOriginalFilename());

        final String ARTICLES_STORAGE_FOLDER = STORAGE_FOLDER + "/articles";

        String filePath = APP_LOCATION
                + ARTICLES_STORAGE_FOLDER + "/"
                + id + "/"
                + newFileName;

        fileService.saveMultipartFile(file, filePath);

        return ARTICLES_STORAGE_FOLDER + "/" + id + "/" + newFileName;
    }

    public void deleteArticleById(UUID id) {
        log.info("Deleting the article by id: " + id);
        Article article = getArticleById(id);
        articleRepository.delete(article);
    }

    public void deleteAllArticles() {
        log.info("Deleting all articles");
        articleRepository.deleteAll();
    }
}
