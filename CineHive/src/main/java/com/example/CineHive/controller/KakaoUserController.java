package com.example.CineHive.controller;

import com.example.CineHive.dto.KakaoUserInfo;
import com.example.CineHive.service.KakaoUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class KakaoUserController {

    @Autowired
    private KakaoUserService kakaoUserService;


    @GetMapping("/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoUserService.getClientId() +
                "&redirect_uri=" + kakaoUserService.getRedirectUri() + "&response_type=code";
        response.sendRedirect(url);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code, HttpServletRequest request) {
        try {
            String accessToken = kakaoUserService.getAccessToken(code);
            KakaoUserInfo userInfo = kakaoUserService.getUserInfo(accessToken);
            kakaoUserService.registerUser(userInfo);

            HttpSession session = request.getSession();
            session.setAttribute("user", userInfo);

            // 성공시 사용자 정보를 JSON 형식으로 응답
            // 나중에 vue 구현할 때 redirectView로 변경 필요
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during Kakao login process");
        }
    }

    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody KakaoUserInfo userInfo, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("user", userInfo); // 사용자 정보를 세션에 저장
        return ResponseEntity.ok("Session created successfully");
    }

    @GetMapping("/kakao/success")
    public ResponseEntity<?> successPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            KakaoUserInfo userInfo = (KakaoUserInfo) session.getAttribute("user");
            if (userInfo != null) {
                return ResponseEntity.ok(userInfo);
            }
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // 카카오 로그아웃 URL 생성
        String logoutUrl = "https://kauth.kakao.com/oauth/logout?client_id=" + kakaoUserService.getClientId() + "&logout_redirect_uri=" + kakaoUserService.getLogoutRedirectUri();

        return ResponseEntity.ok(logoutUrl); // 로그아웃 URL 반환
    }

    @GetMapping("/logout")
    public RedirectView handleLogoutRedirect(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 현재 세션 가져오기
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8080/login"); // 로그인 페이지로 리다이렉트
        return redirectView; // RedirectView 반환
    }

}