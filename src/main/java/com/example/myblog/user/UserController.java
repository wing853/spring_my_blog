package com.example.myblog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    // 로그인
    // 1. 화면 요청
    @GetMapping("/login-form")
    public String loginFormPage() {
        return "user/login-form";
    }

    // 2. 로그인 기능 요청
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO) {
        loginDTO.validate();
        User sessionUser = userRepository.login(loginDTO.getUsername(),loginDTO.getPassword());

        if(sessionUser == null) {
            throw new IllegalArgumentException("사용자 명 또는 비밀번호가 잘못 입력 되었습니다");
        }

        httpSession.setAttribute("sessionUser",sessionUser);

        return "redirect:/";
    }

    @GetMapping("/logout")
    // 로그아웃
    public String logout() {
        httpSession.invalidate();

        return "redirect:/";
    }

    // 회원 가입
    @GetMapping("/join-form")
    public String joinFormPage() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String joinProc(UserRequest.JoinDTO joinDTO) {

        joinDTO.validate();

        User userCheck = userRepository.findByUsername(joinDTO.getUsername());
        if(userCheck !=null) {
            throw new IllegalArgumentException(userCheck.getUsername() + "은 이미 사용중입니다.");
        }
        userRepository.join(joinDTO.toEntity());
        return "redirect:/";
    }
}
