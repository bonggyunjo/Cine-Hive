package com.example.CineHive.controller;

import com.example.CineHive.dto.GoogleUserInfo;
import com.example.CineHive.service.GoogleUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/auth")
public class GoogleUserController {

    @Autowired
    private GoogleUserService googleUserService;

    @GetMapping("/google")
    public void googleLoginRedirect(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + googleUserService.getClientId() +
                "&redirect_uri=" + URLEncoder.encode(googleUserService.getRedirectUri(), "UTF-8") +
                "&response_type=code" +
                "&scope=" + URLEncoder.encode("email profile", "UTF-8"); // 'scope' 값 인코딩 추가

        // URL 검증 (디버깅용)
        System.out.println("Redirect URL: " + redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam String code, HttpServletRequest request) {
        try {
            String accessToken = googleUserService.getAccessToken(code);
            GoogleUserInfo userInfo = googleUserService.getUserInfo(accessToken);
            googleUserService.registerUser(userInfo);

            HttpSession session = request.getSession();
            session.setAttribute("user", userInfo);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request");
        }
    }

    @GetMapping("/google/success")
    public ResponseEntity<?> googleSuccessPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            GoogleUserInfo userInfo = (GoogleUserInfo) session.getAttribute("user");
            if (userInfo != null) {
                return ResponseEntity.ok(userInfo);
            }
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }
}