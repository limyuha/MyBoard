package com.example.newboard.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ArticleUpdateRequest {
    @NotBlank(message = "제목은 필수입니다. 이녀석아")
    private String title;

    @NotBlank(message = "내용 필수")
    private String content;
}
