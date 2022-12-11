package com.nellshark.springbootblog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "published")
    private LocalDate published;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Transient
    private String link;

    public Article(String title, String thumbnail, String content) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.content = content;
        this.published = LocalDate.now();
    }

    public Article(String title, String thumbnail, String content, LocalDate date) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.content = content;
        this.published = date;
    }

    public String getLink() {
        return getTitle().replaceAll("[._~:/?#@!$&'()*+,;=]", "")
                .replace(" ", "-");
    }
}
