package com.example.newboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    // ✅ 성공 응답 (데이터 있을 때)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // ✅ 성공 응답 (메시지만 있을 때)
    public static ApiResponse<Void> successMessage(String message) {
        return new ApiResponse<>(true, null, message);
    }

    // ✅ 실패 응답
    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
