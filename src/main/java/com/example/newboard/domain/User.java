package com.example.newboard.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
// DB 매핑 시 테이블 이름을 "users"로 지정
// 유니크 제약조건 : 동일한 이메일을 가진 유저는 2명 이상 들어갈 수 없음.
@Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String email;

    @Column(nullable=false, length=60)
    private String password; // BCrypt 해시

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, length=20)
    private String role; // "USER"

    private User(String email, String name) {
        this.email = email;
        this.name = name;
        this.password = ""; // OAuth 가입자는 비번 공란 가능
        this.role = "USER";
    }

    public static User create(String email, String name) {
        return new User(email, name);
    }
}