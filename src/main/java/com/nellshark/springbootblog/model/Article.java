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

    @Column(name = "text")
    private String text;

    @Column(name = "published")
    private LocalDate published;

    @Column(name = "image")
    private String image;

    public Article(String title, String text) {
        this.title = title;
        this.text = text;
        this.published = LocalDate.now();
    }

    public Article(String title, String text, LocalDate date) {
        this.title = title;
        this.text = text;
        this.published = date;
    }
}
