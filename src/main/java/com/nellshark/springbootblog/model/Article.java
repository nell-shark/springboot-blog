package com.nellshark.springbootblog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @Column(name = "id")
    @NonNull
    private UUID id;

    @NonNull
    @Column(name = "title")
    private String title;

    @ToString.Exclude
    @NonNull
    @Column(name = "content")
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Builder.Default
    @Column(name = "local_date_time")
    private LocalDateTime localDateTime = LocalDateTime.now();

    @ToString.Exclude
    @Transient
    private String link;

    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    public String getLink() {
        return getTitle()
                .replaceAll("[._~:/?#@!$&'()*+,;=]", "")
                .replace(" ", "-");
    }
}
