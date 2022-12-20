package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import com.nellshark.springbootblog.service.ArticleService;
import com.nellshark.springbootblog.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/articles")
@AllArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final String ARTICLE_TEMPLATES = "/articles";

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String redirectToArticleCreationPage() {
        return "redirect:/articles/" + UUID.randomUUID() + "/create";
    }

    @GetMapping("/{id}/create")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String getArticleCreatingPage(@PathVariable("id") UUID id) {
        return ARTICLE_TEMPLATES + "/create";
    }

    @PostMapping("/{id}/create")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String createArticle(@PathVariable("id") UUID id,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        Article article = articleService.saveArticle(id, title, content, thumbnail);
        return "redirect:/articles/" + article.getLink();
    }

    @PostMapping(value = "/{id}/upload/image", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ResponseEntity<Map<String, String>> uploadImage(@PathVariable("id") UUID id,
                                                           @RequestParam("file") MultipartFile file) throws IOException {
        String image = articleService.saveArticleImage(file, id);
        return new ResponseEntity<>(Map.of("location", image), HttpStatus.OK);
    }

    @GetMapping("/{link}")
    public String getArticlePage(@PathVariable("link") String link, Model model) {
        model.addAttribute("article", articleService.getArticleByLink(link));
        return ARTICLE_TEMPLATES + "/link";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String getArticleEditPage(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return ARTICLE_TEMPLATES + "/edit";
    }

    @PatchMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String updateArticle(@PathVariable("id") UUID id,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        Article article = articleService.saveArticle(id, title, content, thumbnail);
        return "redirect:/articles/" + article.getLink();
    }


    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getListOfArticlesPage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return ARTICLE_TEMPLATES + "/list";
    }

    @PostMapping("/{link}/add-comment")
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public String addCommentToArticle(@PathVariable("link") String link,
                                      @RequestParam("comment") String content,
                                      @AuthenticationPrincipal User user) {
        Article article = articleService.getArticleByLink(link);
        commentService.saveComment(article, user, content);
        return "redirect:/articles/" + link;
    }
}
