package com.nellshark.springbootblog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @NonNull
    private UUID id;

    @Column(name = "title", unique = true, nullable = false)
    @NotEmpty(message = "Title cannot be empty")
    @NonNull
    private String title;

    @Column(name = "content", nullable = false)
    @ToString.Exclude
    @NotEmpty(message = "Content cannot be empty")
    @NonNull
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public Optional<String> getThumbnail() {
        if (thumbnail == null) return Optional.empty();
        return Optional.of("/storage/articles/" + getId() + "/" + thumbnail);
    }
}
