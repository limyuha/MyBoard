package com.example.newboard.repository;

import com.example.newboard.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//public interface ArticleRepository extends JpaRepository<Article, Long> {
//    Optional<Article> findByIdAndAuthor_Email(Long id, String email);
//    long deleteByIdAndAuthor_Email(Long id, String email);
//}

// CRUD 제공
// findAll()
// 데이터 접근에만 집중 하자!
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @EntityGraph(attributePaths = "author")  // 조회 시 작성자(author)도 같이 가져오기
    Optional<Article> findById(Long id);

    // 최신순 정렬, 페이징
    @EntityGraph(attributePaths = "author")
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 동시성 문제를 해결하기 위해 백에서 증가, 조회수 증가 전용
    @Modifying  // update/delete 쿼리 실행할 수 있게 해줌
    // SELECT 전용이라서 update/delete 실행 안 됨
    @Query("update Article a set a.views = a.views + 1 where a.id = :id")  // db가 직접 계산
    int increaseViewCount(@Param("id") Long id);

    //    long deleteByIdAndAuthor_Email(Long id, String email);

    //    @EntityGraph(attributePaths = "author")  // 본인 확인용 조회
//    Optional<Article> findByIdAndAuthor_Email(Long id, String email);
}



