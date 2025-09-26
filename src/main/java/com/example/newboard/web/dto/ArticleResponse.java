package com.example.newboard.web.dto;

import com.example.newboard.domain.Article;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long views;
//    private Long likes;
    private LocalDateTime createdAt;

    // Article 엔티티 객체를 받아서 ArticleResponse DTO 객체로 변환
    // static이므로 객체 생성 없이 ArticleResponse.from(article) 처럼 바로 호출 가능
    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorName(article.getAuthor().getName())
                .views(article.getViews())
                .createdAt(article.getCreatedAt())
                .build();
    }
}
