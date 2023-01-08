package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final String ARTICLE_TEMPLATES = "articles";

    @GetMapping("/create/{id}")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String redirectToArticleCreationPage(@PathVariable(value = "id", required = false) UUID id) {
        if (id == null) return "redirect:/articles/" + UUID.randomUUID() + "/create";
        return ARTICLE_TEMPLATES + "/create";
    }

    @PostMapping("/create/{id}")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public String createArticle(@PathVariable("id") UUID id,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        Article article = articleService.saveArticle(id, title, content, thumbnail);
        return "redirect:/articles/" + article.getId();
    }

    @PostMapping(value = "/upload/image/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ResponseEntity<Map<String, String>> uploadImage(@PathVariable("id") UUID id,
                                                           @RequestParam("file") MultipartFile file) throws IOException {
        String image = articleService.saveArticleImage(file, id);
        return new ResponseEntity<>(Map.of("location", image), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public String getArticlePageById(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return ARTICLE_TEMPLATES + "/id";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String getArticleEditPage(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return ARTICLE_TEMPLATES + "/edit";
    }

    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public String updateArticle(@PathVariable("id") UUID id,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        Article article = articleService.saveArticle(id, title, content, thumbnail);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getListOfArticlesPage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return ARTICLE_TEMPLATES + "/list";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('DELETE_ARTICLES')")
    public String deleteArticle(@PathVariable("id") UUID id) {
        articleService.deleteArticleById(id);
        return "redirect:/articles/list";
    }
}
