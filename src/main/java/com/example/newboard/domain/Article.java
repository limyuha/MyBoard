package com.example.newboard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// JPA에게 "이 클래스는 DB 테이블과 매핑되는 엔티티"임을 알림, 없으면 JPA에서 관리하지 않음
@Entity

// 클래스 필드(멤버 변수)들에 대한 Getter 메서드를 자동 생성
@Getter

// 기본 생성자를 만들어줌
// protected 로 제한 → JPA는 리플렉션으로 객체를 생성할 때 기본 생성자를 필요로 하기 때문에 반드시 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// 모든 필드를 파라미터로 받는 생성자를 만들어줌
// private 으로 막아두면 외부에서 직접 호출 불가 → 대신 @Builder를 통해 객체 생성하도록 강제하는 패턴
@AllArgsConstructor(access = AccessLevel.PRIVATE)

// 빌더 패턴을 자동 생성해줌.
// 복잡한 생성자 대신 체이닝으로 객체를 만들 수 있음.
@Setter
@Builder

// auditing 이벤트 리스너 (날짜)
@EntityListeners(AuditingEntityListener.class)
public class Article {  // 테이블과 매핑되는 그릇
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column  // 조회수
    @Builder.Default  // @Builder는 필드 초기화 = 0L을 무시하기 때문에 기본값 세팅 필요
    private Long views = 0L;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // N:1 (여러 글 → 한 사용자)
    @JoinColumn(name = "author_id", nullable = false)  // 외래키 컬럼명
    private User author;  // User 테이블의 id와 연결되는 외래키 컬럼, User 객체를 반환(롬복의 @getter으로 반환메서드 없어도 됨
    // article.getAuthor() 호출 시 User 객체를 반환

//    // ✅ 작성일 추가
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;

    @CreatedDate   // 작성일, 생성 시 자동 값 주입
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 엔티티가 저장될 때 자동으로 시간 넣기
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 게시글 ↔ 댓글 : 1:N 관계 (양방향)
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default   // 빌더 사용시에도 초기화 보장
    private List<Comment> comments = new ArrayList<>();

    // 양방향 연관관계 편의 메서드(댓글 추가 편의 메서드)
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setArticle(this);  // 양방향 일관성 유지
    }

    // 댓글 삭제 편의 메서드
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setArticle(null);
    }
}
