package com.example.newboard.web.view;

import com.example.newboard.service.UserService;
import com.example.newboard.web.dto.JoinRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthViewController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/join")
    public String joinForm(Model model){
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")  // @Valid :  JoinRequest 클래스에 붙은 (@NotBlank, @Email 등)을 검사, 실패 시 에러 정보가 BindingResult
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult br, Model model){
        if (br.hasErrors()) return "join";  // 에러가 있으면 br.hasErrors() → true, 다시 join.html을 보여줌
        try {  // 검증을 통과한 경우 서비스 로직 호출
            userService.join(joinRequest);
        } catch (IllegalArgumentException e){  // 예외 발생 시 템플릿에서 ${error}로 출력 가능
            model.addAttribute("error", e.getMessage());
            return "join";
        }
        return "redirect:/login";
    }
}