package com.nellshark.springbootblog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Article article;

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @NonNull
    @Column(name = "content")
    @NotEmpty(message = "Comment cannot be empty")
    private String content;

    @Column(name = "local_date_time")
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", article.title=" + article.getTitle() +
                ", user.email=" + user.getEmail() +
                ", content=[" + content + "]" +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
