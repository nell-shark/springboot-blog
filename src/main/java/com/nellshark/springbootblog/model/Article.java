package com.nellshark.springbootblog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article implements Comparable<Article> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private LocalDate date;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "image")
    private byte[] imageBytes;

    @Transient
    public String image() {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        return Base64Utils.encodeToString(imageBytes);
    }

    public Article(String title, String text, LocalDate date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Article(String title, String text, LocalDate date, byte[] imageBytes) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.imageBytes = imageBytes;
    }

    @Override
    public int compareTo(Article article) {
        return this.id.compareTo(article.id);
    }
}
