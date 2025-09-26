package com.example.newboard.web.api;

import com.example.newboard.domain.Article;
import com.example.newboard.service.ArticleService;
import com.example.newboard.service.security.SecurityUtil;
import com.example.newboard.web.dto.ApiResponse;
import com.example.newboard.web.dto.ArticleCreateRequest;
import com.example.newboard.web.dto.ArticleResponse;
import com.example.newboard.web.dto.ArticleUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")

public class ArticleApiController {
    private final ArticleService articleService;

    // ✅ 게시글 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> create(@Valid @RequestBody ArticleCreateRequest req,  // JSON 요청 바디를 ArticleCreateRequest DTO로 매핑
                                                  Authentication auth) {  // 로그인된 사용자의 인증 정보 (작성자 이메일 꺼낼 때 사용)
        System.out.println("👉 Authentication class=" + auth.getClass().getName());
        System.out.println("👉 auth.getName()=" + auth.getName());
        System.out.println("👉 auth.getPrincipal()=" + auth.getPrincipal());

        String email = SecurityUtil.extractEmail(auth); // ✅ email 안전하게 추출
//        Long id = articleService.create(req, email); // // DB에 새 게시글 저장 후 id 반환
//        ArticleResponse response = articleService.getArticle(id); // 생성 후 DTO(ArticleResponse)로 변환, 조회수 증가 없는 메소드 호출
        // 201 Created 상태 코드 + Location 헤더(/articles/{id}) 설정
//        return ResponseEntity.created(URI.create("/articles/" + id)).body(response);  // .body(response) => 생성된 게시글의 DTO(JSON) 응답

        ArticleResponse response = articleService.create(req, email); // ✅ 바로 DTO 받음
//        return ResponseEntity.created(URI.create("/articles/" + response.getId()))
//                .body(response);
        return ResponseEntity
                .created(URI.create("/articles/" + response.getId()))
                .body(ApiResponse.success(response));
    }

    // ✅ 게시글 수정
    @PutMapping("/{id}")  // public ResponseEntity<ArticleResponse>
    public ResponseEntity<ApiResponse<ArticleResponse>> update(
                                                  @PathVariable Long id,  // URL 경로의 id 값 추출
                                                  @Valid @RequestBody ArticleUpdateRequest req,
                                                  Authentication auth) {
        String email = SecurityUtil.extractEmail(auth);
//        articleService.update(id, email, req);  // 권한 체크 후 게시글 수정
//        ArticleResponse response = articleService.getArticle(id); // 수정 후 DTO 반환
        ArticleResponse response = articleService.update(id, email, req); // ✅ 바로 DTO 반환
//        return ResponseEntity.ok(response);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    // ✅ 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication auth) {
        String email = SecurityUtil.extractEmail(auth);
        articleService.delete(id, email);
//        return ResponseEntity.noContent().build();  // 삭제 성공 시 아무 내용 없는 204 응답 반환
        return ResponseEntity.ok(ApiResponse.successMessage("게시글이 삭제되었습니다."));
    }

    // ✅ 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticle(@PathVariable Long id) {
//        return ResponseEntity.ok(articleService.getArticle(id));  // articleService.getArticle(id) -> id로 게시글 찾아서 DTO 변환
        ArticleResponse response = articleService.getArticle(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
