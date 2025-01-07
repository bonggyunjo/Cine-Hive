package com.example.CineHive.controller;

import com.example.CineHive.dto.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        // 사용자 등록 서비스 호출
        boolean isRegistered = userService.registerUser(userDto);

        if (isRegistered) {
            // 사용자 등록 성공 시 HTTP 201 Created 응답
            return ResponseEntity.status(201).body("User registered successfully.");
        } else {
            // 실패 시 HTTP 400 Bad Request 응답
            return ResponseEntity.badRequest().body("Failed to register user.");
        }
    }
}
