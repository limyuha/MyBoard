package com.example.newboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String uri = request.getRequestURI();

        // API 요청일 경우 ("/api"로 시작하면 JSON 반환)
        if (uri.startsWith("/api")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "로그인이 필요한 서비스입니다.");

            response.getWriter().write(objectMapper.writeValueAsString(body));
        }
        // 일반 웹 요청일 경우 (리다이렉트)
        else {
            response.sendRedirect("/login");
        }
    }
}

