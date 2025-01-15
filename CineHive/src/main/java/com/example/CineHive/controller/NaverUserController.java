package com.example.CineHive.controller;

import com.example.CineHive.dto.NaverUserInfo;
import com.example.CineHive.dto.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.UserRepository;
import com.example.CineHive.service.NaverUserService;
import com.example.CineHive.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class NaverUserController {

    @Autowired
    private final NaverUserService naverUserService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserRepository userRepository;

    @GetMapping("/naver")
    public void naverLoginRedirect(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + naverUserService.getClientId() +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8081/api/auth/naver/callback", "UTF-8") +
                "&state=" + UUID.randomUUID().toString() +
                "&scope=name,email,gender,nickname,phone"; // 필요한 스코프 추가
        response.sendRedirect(redirectUrl);
    }


    @GetMapping("/naver/callback")
    public void naverCallback(@RequestParam String code, @RequestParam String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accessToken = naverUserService.getAccessToken(code);
            NaverUserInfo userInfo = naverUserService.getUserInfo(accessToken);
            if (!userService.checkUserExistsNaver(userInfo.getNaverId())) {
                naverUserService.registerUser(userInfo);
                HttpSession session = request.getSession();
                session.setAttribute("user", userInfo);
                // 클라이언트의 추가 정보 입력 화면으로 리다이렉트 (loginType을 쿼리 파라미터로 추가)
                response.sendRedirect("http://localhost:8080/additional-info?loginType=naver"); // 수정된 부분
            } else {
                // 사용자가 이미 가입한 경우 홈으로 리다이렉트
                HttpSession session = request.getSession();
                session.setAttribute("user", userInfo); // 세션에 사용자 정보 저장
                response.sendRedirect("http://localhost:8080/"); // 홈 화면으로 리다이렉트
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during naver login process");
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

    @PostMapping("/naver/register")
    public ResponseEntity<String> registerUserDetails(@RequestBody UserDto userDto) {
        User newUser = new User();
        newUser.setMemUserid(userDto.getMemUserid());
        newUser.setMemEmail(userDto.getMemEmail());
        newUser.setMemPw(userDto.getMemPassword());
        newUser.setMemNickname(userDto.getMemNickname());
        newUser.setMemName(userDto.getMemName());
        newUser.setMemPhone(userDto.getMemPhone());
        newUser.setMemSex(userDto.getMemSex());
        newUser.setNaverId(userDto.getNaverId()); // 카카오 ID 추가
        newUser.setMemRegisterDatetime(LocalDateTime.now());
        newUser.setMemType("네이버"); // 가입 유형 설정
        newUser.setGenres(userDto.getGenres());

        userRepository.save(newUser);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @GetMapping("/naver/check-user")
    public ResponseEntity<Boolean> checkUser(@RequestParam String naverId) {
        boolean exists = userService.checkUserExistsNaver(naverId);
        return ResponseEntity.ok(exists);
    }
}