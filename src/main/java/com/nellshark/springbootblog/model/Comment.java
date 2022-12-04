package com.nellshark.springbootblog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    public Comment(Article article, User user, String content) {
        this.article = article;
        this.user = user;
        this.content = content;
        this.published = LocalDate.now();
    }

    @Column(name = "published")
    private LocalDate published;

    public Comment(Article article, User user, String content, LocalDate published) {
        this.article = article;
        this.user = user;
        this.content = content;
        this.published = published;
    }
}
