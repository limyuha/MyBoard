package com.example.newboard.service;

import com.example.newboard.domain.Article;
import com.example.newboard.exception.ArticleNotFoundException;
import com.example.newboard.repository.ArticleRepository;
import com.example.newboard.repository.UserRepository;
import com.example.newboard.web.dto.ArticleCreateRequest;
import com.example.newboard.web.dto.ArticleResponse;
import com.example.newboard.web.dto.ArticleUpdateRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    // 페이징 + 최신순 (뷰 전용: 엔티티 반환)
    @EntityGraph(attributePaths = {"author"})  // findAll 할 때 Article + User(author) 를 join 해서 한 번에 가져옴(글쓴이 가져오기위함) + N+1 문제 방지
    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 게시글 생성
    @Transactional
    public ArticleResponse create(ArticleCreateRequest req, String email){
        System.out.println("👉 ArticleService.create() email=" + email);

        var author = userRepository.findByEmail(email)  // 작성자는 이메일로 조회
//                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
                  .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 사용자가 존재하지 않습니다: " + email));

        Article article = articleRepository.save(
                Article.builder()
                        .title(req.getTitle())
                        .content(req.getContent())
                        .author(author)
                        .build()
        );
        return ArticleResponse.from(article);
    }


    // N+1 방지 위해 author 함께 로드
    @EntityGraph(attributePaths = {"author"})
    @Transactional(readOnly = true)
    public Article findById(long id) {
        return articleRepository.findById(id)  // select * from article where id = ?
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    // 조회 + 조회수 증가 (뷰용) -> 엔티티 반환
    @Transactional
    public Article viewArticle(Long id) {
        articleRepository.increaseViewCount(id);
        return findById(id);  // 증가 후 findById로 최신 상태 조회
    }

    @Transactional
    public ArticleResponse update(Long id, String email, ArticleUpdateRequest req){
        var article = findById(id); // findById로 글 찾고
        if (!article.getAuthor().getEmail().equals(email)) {  // 작성자 확인 → 본인만 수정 가능
            throw new AccessDeniedException("Not your article. 본인 글이 아닙니다.");
        }

        article.update(req.getTitle(), req.getContent());  // 엔티티 메서드 update()로 수정
        return ArticleResponse.from(article); // ✅ 수정된 엔티티를 바로 DTO 변환
    }

    @Transactional
    public void delete(Long id, String email){
        var article = findById(id); // 서비스의 공통 조회 메서드 사용
        if (!article.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("본인 글이 아닙니다.");
        }
        articleRepository.delete(article); // JpaRepository 기본 메서드 사용
    }

    // 단순 조회 + DTO 반환 , API용(조회수 증가 X)
    @Transactional(readOnly = true)
    public ArticleResponse getArticle(Long id) {
        return ArticleResponse.from(findById(id));
    }
}
