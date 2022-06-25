package com.nellshark.springbootblog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "articles")
public class Article implements Comparable<Article> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

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
        return Base64Utils.encodeToString(imageBytes);
    }

    @Override
    public int compareTo(Article article) {
        return this.id.compareTo(article.id);
    }
}
