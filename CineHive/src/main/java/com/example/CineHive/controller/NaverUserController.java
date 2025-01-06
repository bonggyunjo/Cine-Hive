package com.example.CineHive.controller;

import com.example.CineHive.dto.NaverUserInfo;
import com.example.CineHive.service.NaverUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class NaverUserController {

    @Autowired
    private final NaverUserService naverUserService;

    @GetMapping("/naver")
    public void naverLoginRedirect(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + naverUserService.getClientId() +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8081/api/auth/naver/callback", "UTF-8") +
                "&state=" + UUID.randomUUID().toString();
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<?> naverCallback(@RequestParam String code, @RequestParam String state, HttpServletRequest request) {
        try {
            String accessToken = naverUserService.getAccessToken(code);
            NaverUserInfo userInfo = naverUserService.getUserInfo(accessToken);
            naverUserService.registerUser(userInfo);

            HttpSession session = request.getSession();
            session.setAttribute("user", userInfo);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request");
        }
    }

    @GetMapping("/naver/success")
    public ResponseEntity<?> naverSuccessPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            NaverUserInfo userInfo = (NaverUserInfo) session.getAttribute("user");
            if (userInfo != null) {
                return ResponseEntity.ok(userInfo);
            }
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }
}