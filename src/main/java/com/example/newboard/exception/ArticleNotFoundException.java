package com.example.newboard.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(Long id) {
        super("게시글을 찾을 수 없습니다. id=" + id);
    }
}
