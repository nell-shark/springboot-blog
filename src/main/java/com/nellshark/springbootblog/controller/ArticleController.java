package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.Comment;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.ArticleService;
import com.nellshark.springbootblog.service.CommentService;
import com.nellshark.springbootblog.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/articles")
@AllArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final String TEMPLATES_FOLDER = "articles" + File.separator;

    //TODO: get method
    private final String ARTICLE_STORAGE_FOLDER = File.separator + "storage" + File.separator
            + "articles" + File.separator
            + "%d" + File.separator;

    @GetMapping("/{link}")
    public String getArticlePage(@PathVariable String link,
                                 Model model) {
        Article article = articleService.getArticleByLink(link);
        model.addAttribute("article", article);
        return TEMPLATES_FOLDER + "link";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String getCreateArticlePage() {
        return TEMPLATES_FOLDER + "create";
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getListOfArticlesPage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return TEMPLATES_FOLDER + "list";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String createArticle(@RequestParam("title") String title,
                                @RequestParam("thumbnail") MultipartFile thumbnail,
                                @RequestParam("content") String content) throws IOException {
        Article article = new Article();
        article.setId(articleService.getNextSeriesId());
        article.setTitle(title);
        article.setContent(content);

        FileUtils.saveArticleImage(thumbnail, article);

        // TODO CHANGE FILE SEPARATOR
        String thumbnailPath = String.format(ARTICLE_STORAGE_FOLDER, articleService.getNextSeriesId())
                + thumbnail.getOriginalFilename();
        article.setThumbnail(thumbnailPath);

        articleService.saveArticle(article);
        return "redirect:/";
    }

    @PostMapping(value = "/upload-image", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        Article article = new Article();
        article.setId(articleService.getNextSeriesId());

        try {
            FileUtils.saveArticleImage(file, article);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return new ResponseEntity<>(
                Map.of("location",
                        String.format(ARTICLE_STORAGE_FOLDER, article.getId()).replace(File.separator, "/")
                                + file.getOriginalFilename()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{link}/edit")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String getEditArticlePage(@PathVariable String link, Model model) {
        model.addAttribute("article", articleService.getArticleByLink(link));
        return TEMPLATES_FOLDER + "edit";
    }

    @PatchMapping("/{link}/edit")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String updateArticle(@PathVariable String link,
                                Model model,
                                @RequestParam("title") String title,
                                @RequestParam("thumbnail") MultipartFile thumbnail,
                                @RequestParam("content") String content) throws IOException {
        Article updatedArticle = new Article();
        updatedArticle.setTitle(title);

        log.warn(thumbnail.toString());

        if (!thumbnail.isEmpty()) {
            FileUtils.saveArticleImage(thumbnail, updatedArticle);

            // TODO CHANGE FILE SEPARATOR
            String thumbnailPath = String.format(ARTICLE_STORAGE_FOLDER, articleService.getNextSeriesId())
                    + thumbnail.getOriginalFilename();
            updatedArticle.setThumbnail(thumbnailPath);
        }

        updatedArticle.setContent(content);

        articleService.updateArticle(link, updatedArticle);

        return "redirect:/articles/" + link;
    }

    @PostMapping("/{link}/add-comment")
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public String addCommentToArticle(@PathVariable String link,
                                      @RequestParam("comment") String comment,
                                      @AuthenticationPrincipal User user) {
        Article articleByTitle = articleService.getArticleByLink(link);
        commentService.saveComment(new Comment(articleByTitle, user, comment));
        return "redirect:/articles/" + link;
    }
}
