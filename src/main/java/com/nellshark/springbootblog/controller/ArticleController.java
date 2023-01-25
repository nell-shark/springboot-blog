package com.nellshark.springbootblog.controller;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
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
    public ModelAndView redirectToArticleCreatePageWithRandomUUID() {
        return new ModelAndView("redirect:/articles/create/" + UUID.randomUUID());
    }

    @GetMapping("create/{id}")
    @PreAuthorize("hasAuthority('CREATE_ARTICLES')")
    public ModelAndView getArticleCreatePage(@PathVariable("id") UUID id) {
        return new ModelAndView("articles/create")
                .addObject("newArticle", new Article());
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
                                      @ModelAttribute("newArticle") @Valid Article article,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "file", required = false) MultipartFile thumbnail) throws IOException {
        if (bindingResult.hasErrors()) return new ModelAndView("redirect:/articles/create");
        articleService.save(article, thumbnail);
        return new ModelAndView("redirect:/articles/" + id);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasAuthority('EDIT_ARTICLES')")
    public ModelAndView updateArticle(@PathVariable("id") UUID id,
                                      @ModelAttribute("article") @Valid Article updatedArticle,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "file", required = false) MultipartFile thumbnail) throws IOException {
        if (bindingResult.hasErrors()) return new ModelAndView("redirect:/articles/edit/" + id);
        articleService.save(updatedArticle, thumbnail);
        return new ModelAndView("redirect:/articles/" + id);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('DELETE_ARTICLES')")
    public ModelAndView deleteArticleById(@PathVariable("id") UUID id) {
        articleService.deleteArticleById(id);
        return new ModelAndView("redirect:/articles");
    }
}
