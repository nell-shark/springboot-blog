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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/articles")
@AllArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final String TEMPLATES_FOLDER = "articles" + File.separator;
    private final String ARTICLE_STORAGE_FOLDER = File.separator + "storage" + File.separator
            + "articles" + File.separator
            + "%d" + File.separator;

    @GetMapping("/{link}")
    public String getArticlePage(@PathVariable String link,
                                 Model model,
                                 @AuthenticationPrincipal User user) {
        if (user != null) model.addAttribute("user", user);
        Article article = articleService.getArticleByLink(link);
        model.addAttribute("article", article);
        List<Comment> comments = commentService.getAllCommentsByArticle(article);
        model.addAttribute("comments", comments);
        return TEMPLATES_FOLDER + "article";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String getCreateArticlePage(Model model,
                                       @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return TEMPLATES_FOLDER + "create";
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

    @PostMapping("/{link}/add-comment")
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public String addCommentToArticle(@PathVariable String link,
                                      @RequestParam("comment") String comment,
                                      Model model,
                                      @AuthenticationPrincipal User user) {
        Article articleByTitle = articleService.getArticleByLink(link);
        commentService.saveComment(new Comment(articleByTitle, user, comment));
        model.addAttribute("user", user);
        return "redirect:/articles/" + link;
    }
}
