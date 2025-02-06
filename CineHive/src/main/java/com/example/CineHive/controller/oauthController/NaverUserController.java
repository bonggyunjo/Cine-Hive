package com.example.CineHive.controller.oauthController;

import com.example.CineHive.dto.oauth.NaverUserInfo;
import com.example.CineHive.dto.user.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.entity.oauth.KakaoUser;
import com.example.CineHive.entity.oauth.NaverUser;
import com.example.CineHive.repository.NaverUserRepository;
import com.example.CineHive.repository.UserRepository;
import com.example.CineHive.service.oauth.NaverUserService;
import com.example.CineHive.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private final NaverUserRepository naverUserRepository;
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

            NaverUser naverUser = naverUserRepository.findByNaverId(userInfo.getNaverId()).orElse(null);

            if (naverUser == null) {
                System.out.println("GoogleUser is null for Google ID: " + userInfo.getNaverId());
                naverUser = naverUserService.registerNewNaverUser(userInfo);
            } else {
                System.out.println("GoogleUser found: " + naverUser.getName() + ", " + naverUser.getGenres());
            }

            userInfo.setName(naverUser.getName());
            userInfo.setGenres(naverUser.getGenres());

            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(userInfo));


            if (userService.checkUserExistsNaver(userInfo.getNaverId())) {
                // 기존 회원인 경우
                HttpSession session = request.getSession();
                session.setAttribute("user", userInfo); // 세션에 사용자 정보 저장
                response.sendRedirect("http://localhost:8080/"); // 홈 화면으로 리다이렉트
            } else {
                // 소셜 회원인 경우 추가 정보 입력 화면으로 리다이렉트
                naverUserService.registerUser(userInfo); // 사용자 정보 저장
                HttpSession session = request.getSession();
                session.setAttribute("user", userInfo); // 세션에 사용자 정보 저장
                response.sendRedirect("http://localhost:8080/additional-info?loginType=naver"); // 추가 정보 입력 화면으로 리다이렉트
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

        NaverUser naverUser = naverUserRepository.findByNaverId(userDto.getNaverId())
                .orElseThrow(() -> new IllegalArgumentException("Kakao User not found"));
        naverUser.setName(userDto.getMemName());
        naverUser.setGenres(userDto.getGenres());
        naverUserRepository.save(naverUser);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @GetMapping("/naver/check-user")
    public ResponseEntity<Boolean> checkUser(@RequestParam String naverId) {
        boolean exists = userService.checkUserExistsNaver(naverId);
        return ResponseEntity.ok(exists);
    }
}