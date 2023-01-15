package com.nellshark.springbootblog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    @NotEmpty(message = "Comment cannot be empty")
    private String content;

    @Column(name = "local_date_time", nullable = false)
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
