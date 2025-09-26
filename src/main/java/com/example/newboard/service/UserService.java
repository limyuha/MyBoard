package com.example.newboard.service;

import com.example.newboard.domain.User;
import com.example.newboard.repository.UserRepository;
import com.example.newboard.web.dto.JoinRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(JoinRequest req){  // 회원가입 폼에서 작성한 데이터 가져왔음
        if (userRepository.existsByEmail(req.getEmail())) // existsByEmail : 같은 이메일이 이미 DB에 있으면 true 반환
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");  // 여기서 던진 IllegalArgumentException은 컨트롤러에서 try-catch 문에 catch로 잡음
        userRepository.save(User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .role("USER")
                .build());
    }
}