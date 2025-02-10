package com.example.CineHive.controller;

import com.example.CineHive.dto.user.LoginDto;
import com.example.CineHive.dto.user.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.UserRepository;
import com.example.CineHive.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {

            // 사용자 등록 서비스 호출
            boolean isRegistered = userService.registerUser(userDto);

            if (isRegistered) {
                // 사용자 등록 성공 시 HTTP 201 Created 응답
                return ResponseEntity.status(201).body("성공적으로 회원가입했습니다!");
            } else {
                // 실패 시 HTTP 400 Bad Request 응답
                return ResponseEntity.badRequest().body("회원가입 실패. 다시 시도해 주세요!");
            }
        } catch (IllegalArgumentException e) {
            // 중복된 값이 있을 경우 오류 메시지 반환 및 로그 출력
            log.error("회원가입 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 일반적인 예외 처리 (선택 사항)
            log.error("예기치 않은 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }





    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginRequest) {
        try {
            boolean loginSuccess = userService.loginUser(loginRequest.getMemUserid(), loginRequest.getMemPassword());
            if (loginSuccess) {
                // 사용자 정보를 가져와서 응답 생성
                User user = userService.getUserInfo(loginRequest.getMemUserid());
                Map<String, Object> response = new HashMap<>();
                response.put("message", "로그인 성공");
                response.put("user", new HashMap<String, Object>() {{
                    put("memUserid", user.getMemUserid());
                    put("name", user.getMemName());
                    put("nickname", user.getMemNickname());
                    put("email", user.getMemEmail());
                    put("genres", user.getGenres());
                }});

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인 실패"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }



    @GetMapping("/checkuserId/{memUserid}")
    public ResponseEntity<Boolean> checkUserId(@PathVariable(value="memUserid") String memUserid) {
        Optional<User> existingUser = userRepository.findByMemUserid(memUserid);
        boolean isAvailable = existingUser.isEmpty(); // 사용자 ID가 존재하지 않으면 사용 가능
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/checknickname/{memNickname}")
    public ResponseEntity<Boolean> checkmemNickname(@PathVariable(value="memNickname") String memNickname) {
        Optional<User> existingUser = userRepository.findByMemNickname(memNickname);
        boolean isAvailable = existingUser.isEmpty(); // 사용자 ID가 존재하지 않으면 사용 가능
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/checkemail/{memEmail}")
    public ResponseEntity<Boolean> checkmemEmail(@PathVariable(value="memEmail") String memEmail) {
        Optional<User> existingUser = userRepository.findByMemEmail(memEmail);
        boolean isAvailable = existingUser.isEmpty(); // 사용자 ID가 존재하지 않으면 사용 가능
        return ResponseEntity.ok(isAvailable);
    }
}