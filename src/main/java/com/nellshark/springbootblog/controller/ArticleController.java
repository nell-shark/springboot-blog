package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("articles")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ModelAndView getIndexPage(@RequestParam(value = "search", required = false) String search) {
        List<Article> articles = StringUtils.isEmpty(search)
                ? articleService.getAllArticles()
                : articleService.doSearch(search);

        return new ModelAndView("articles/index")
                .addObject("search", search)
                .addObject("articles", articles);
    }

    @GetMapping("create")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ModelAndView redirectToArticleCreatePage() {
        return new ModelAndView("redirect:/articles/create/" + UUID.randomUUID());
    }

    @GetMapping("create/{id}")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ModelAndView getArticleCreatePage(@PathVariable("id") UUID id) {
        return new ModelAndView("articles/create");
    }


    @GetMapping("{id}")
    public ModelAndView getArticlePageById(@PathVariable("id") UUID id) {
        return new ModelAndView("articles/id")
                .addObject("article", articleService.getArticleById(id));
    }

    @GetMapping("edit/{id}")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public ModelAndView getArticleEditPage(@PathVariable("id") UUID id) {
        return new ModelAndView("articles/edit")
                .addObject("article", articleService.getArticleById(id));
    }

    @PostMapping("{id}")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ModelAndView createArticle(@PathVariable("id") UUID id,
                                      @RequestParam("title") String title,
                                      @RequestParam("content") String content,
                                      @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        Article article = articleService.saveOrUpdate(id, title, content, thumbnail);
        return new ModelAndView("redirect:/articles/" + article.getId());
    }

    @PostMapping("{id}/image")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ResponseEntity<Map<String, String>> uploadImage(@PathVariable("id") UUID id,
                                                           @RequestParam("file") MultipartFile file) throws IOException {
        String image = articleService.saveArticleImage(file, id);
        return new ResponseEntity<>(Map.of("location", image), HttpStatus.OK);
    }

    // TODO: ModelAttribute
    @PatchMapping("{id}")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public ModelAndView updateArticle(@PathVariable("id") UUID id,
                                      @RequestParam("title") String title,
                                      @RequestParam("content") String content,
                                      @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        Article article = articleService.saveOrUpdate(id, title, content, thumbnail);
        return new ModelAndView("redirect:/articles/" + article.getId());
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('DELETE_ARTICLES')")
    public ModelAndView deleteArticle(@PathVariable("id") UUID id) {
        articleService.deleteArticleById(id);
        return new ModelAndView("redirect:/articles/");
    }
}
