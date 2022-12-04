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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/articles")
@AllArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/{title}")
    public String getArticle(@PathVariable String title,
                             Model model,
                             @AuthenticationPrincipal User user) {
        if (user != null) model.addAttribute("user", user);
        Article articleByTitle = articleService.getByTitle(String.join(" ", title.split("-")));
        List<Comment> comments = commentService.getAllCommentsByArticle(articleByTitle);
        model.addAttribute("article", articleByTitle);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/create-new-article")
    @PreAuthorize("hasAuthority('CREATE_NEW_ARTICLES')")
    public String createNewArticle(Model model,
                                   @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "create-new-article";
    }

    @PostMapping("/create-new-article")
    @PreAuthorize("hasAuthority('CREATE_NEW_ARTICLES')")
    public String postNewArticle(@RequestParam("title") String title,
                                 @RequestParam("image") MultipartFile file,
                                 @RequestParam("text") String text) {
        String folderName = "articles" + File.separator + articleService.getNextSeriesId().toString();
        FileUtils.saveMultipartFileToStorage(file, folderName);
        articleService.saveArticle(new Article(title, file.getOriginalFilename(), text));
        return "redirect:/";
    }

    @PostMapping("/upload-image")
    @PreAuthorize("hasAuthority('CREATE_NEW_ARTICLES')")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        boolean isFileSaved = FileUtils.saveMultipartFileToStorage(file, articleService.getNextSeriesId().toString());

        if (!isFileSaved) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return ResponseEntity.ok("File uploaded successfully.");
    }

    @PostMapping("/{title}/add-comment")
    @PreAuthorize("hasAuthority('WRITE_COMMENTS')")
    public String addComment(@PathVariable String title,
                             @RequestParam("comment") String comment,
                             Model model,
                             @AuthenticationPrincipal User user) {
        Article articleByTitle = articleService.getByTitle(String.join(" ", title.split("-")));
        commentService.saveComment(new Comment(articleByTitle, user, comment));
        model.addAttribute("user", user);
        return "redirect:/articles/" + title;
    }
}
