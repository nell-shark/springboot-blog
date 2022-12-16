package com.nellshark.springbootblog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private Article article;

    @ManyToOne
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "published")
    private LocalDate published;

    public Comment(Article article, User user, String content) {
        this.article = article;
        this.user = user;
        this.content = content;
        this.published = LocalDate.now();
    }

    public Comment(Article article, User user, String content, LocalDate published) {
        this.article = article;
        this.user = user;
        this.content = content;
        this.published = published;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", article.id=" + article.getId() +
                ", user.id=" + user.getId() +
                ", content='" + content + '\'' +
                ", published=" + published +
                '}';
    }
}
