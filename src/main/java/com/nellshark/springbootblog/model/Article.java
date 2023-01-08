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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nellshark.springbootblog.service.FileService.STORAGE_FOLDER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    public Optional<String> getThumbnail() {
        if (thumbnail == null) return Optional.empty();
        return Optional.of(STORAGE_FOLDER + "/articles/" + getId() + "/" + thumbnail);
    }
}
