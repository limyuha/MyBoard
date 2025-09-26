package com.example.newboard.repository;


import com.example.newboard.domain.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글 ID(articleId)로 댓글 조회
    @EntityGraph(attributePaths = {"author", "article"})  // author까지 한 번에 같이 select, 마이페이지 위해서 article도 가져옴
    List<Comment> findByArticle_Id(Long articleId); // 언더스코어(_)를 써서 JPA가 article.id라는 의미로 해석하게 한다.
}

