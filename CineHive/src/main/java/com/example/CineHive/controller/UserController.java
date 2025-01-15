package com.example.CineHive.controller;

import com.example.CineHive.dto.LoginRequest;
import com.example.CineHive.dto.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.UserRepository;
import com.example.CineHive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
            // 중복 체크
            userService.checkDuplicateUserId(userDto.getMemUserid());
            userService.checkDuplicateEmail(userDto.getMemEmail());
            userService.checkDuplicateNickname(userDto.getMemNickname());

            // 사용자 등록 서비스 호출
            boolean isRegistered = userService.registerUser(userDto);

            if (isRegistered) {
                // 사용자 등록 성공 시 HTTP 201 Created 응답
                return ResponseEntity.status(201).body("성공적으로 회원가입했습니다!.");
            } else {
                // 실패 시 HTTP 400 Bad Request 응답
                return ResponseEntity.badRequest().body("회원가입 실패. 다시 시도해주세!");
            }
        } catch (IllegalArgumentException e) {
            // 중복된 값이 있을 경우 오류 메시지 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            boolean loginSuccess = userService.loginUser(loginRequest.getMemUserid(), loginRequest.getMemPassword());
            if (loginSuccess) {
                return ResponseEntity.ok("로그인 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
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