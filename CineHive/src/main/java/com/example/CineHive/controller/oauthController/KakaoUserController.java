package com.example.CineHive.controller.oauthController;

import com.example.CineHive.dto.oauth.KakaoUserInfo;
import com.example.CineHive.dto.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.UserRepository;
import com.example.CineHive.service.oauthService.KakaoUserService;
import com.example.CineHive.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class KakaoUserController {

    @Autowired
    private KakaoUserService kakaoUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoUserService.getClientId() +
                "&redirect_uri=" + kakaoUserService.getRedirectUri() + "&response_type=code";
        response.sendRedirect(url);
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accessToken = kakaoUserService.getAccessToken(code);
            KakaoUserInfo userInfo = kakaoUserService.getUserInfo(accessToken);

            // 사용자 존재 여부 확인
            if (!userService.checkUserExists(userInfo.getKakaoId())) {
                // 사용자가 가입하지 않은 경우
                kakaoUserService.registerUser(userInfo); // 사용자 정보 저장

                HttpSession session = request.getSession();
                session.setAttribute("user", userInfo);

                // 클라이언트의 추가 정보 입력 화면으로 리다이렉트 (loginType을 쿼리 파라미터로 추가)
                response.sendRedirect("http://localhost:8080/additional-info?loginType=kakao"); // 수정된 부분
            } else {
                // 사용자가 이미 가입한 경우 홈으로 리다이렉트
                HttpSession session = request.getSession();
                session.setAttribute("user", userInfo); // 세션에 사용자 정보 저장
                response.sendRedirect("http://localhost:8080/"); // 홈 화면으로 리다이렉트
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during Kakao login process");
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
        log.info("Session exists: {}", session != null); // 세션 존재 여부 로그

        if (session != null) {
            KakaoUserInfo userInfo = (KakaoUserInfo) session.getAttribute("user");
            log.info("User info in session: {}", userInfo); // 세션에 저장된 사용자 정보 로그

            if (userInfo != null) {
                return ResponseEntity.ok(userInfo);
            }
        }
        log.warn("Unauthorized access attempt"); // 인증 실패 로그
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

    @GetMapping("/kakao/check-user")
    public ResponseEntity<Boolean> checkUser(@RequestParam String kakaoId) {
        boolean exists = userService.checkUserExists(kakaoId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/kakao/register")
    public ResponseEntity<String> registerUserDetails(@RequestBody UserDto userDto) {
        User newUser = new User();
        newUser.setMemUserid(userDto.getMemUserid());
        newUser.setMemEmail(userDto.getMemEmail());
        newUser.setMemPw(userDto.getMemPassword());
        newUser.setMemNickname(userDto.getMemNickname());
        newUser.setMemName(userDto.getMemName());
        newUser.setMemPhone(userDto.getMemPhone());
        newUser.setMemSex(userDto.getMemSex());
        newUser.setKakaoId(userDto.getKakaoId()); // 카카오 ID 추가
        newUser.setMemRegisterDatetime(LocalDateTime.now());
        newUser.setMemType("카카오"); // 가입 유형 설정
        newUser.setGenres(userDto.getGenres());

        userRepository.save(newUser);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

}