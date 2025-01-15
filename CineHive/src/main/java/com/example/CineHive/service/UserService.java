package com.example.CineHive.service;

import com.example.CineHive.dto.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(UserDto userDto) {
        User user= new User();
        user.setMemEmail(userDto.getMem_email());
        user.setMemUserid(userDto.getMem_userid());
        user.setMem_pw(passwordEncoder.encode(userDto.getMem_password()));
        user.setMem_name(userDto.getMem_name());
        user.setMem_sex(userDto.getMem_sex());
        user.setMem_phone(userDto.getMem_phone());
        user.setMemNickname(userDto.getMem_nickname());
        user.setMem_type(userDto.getMem_type());
        user.setMem_register_datetime(LocalDateTime.now());
        user.setMem_type("일반");
        // 사용자 정보 저장
        userRepository.save(user);

        return true;
    }

    public void checkDuplicateUserId(String memUserid) {
        Optional<User> existingUser = userRepository.findByMemUserid(memUserid);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 ID입니다.");
        }
    }

    public void checkDuplicateEmail(String memEmail) {
        Optional<User> existingUser = userRepository.findByMemEmail(memEmail);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    public void checkDuplicateNickname(String memNickname) {
        Optional<User> existingUser = userRepository.findByMemNickname(memNickname);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    public boolean loginUser(String mem_userid, String mem_password) {
        // 사용자 ID로 사용자 조회
        Optional<User> existingUser = userRepository.findByMemUserid(mem_userid);

        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        User user = existingUser.get();

        // 비밀번호 비교
        if (!passwordEncoder.matches(mem_password, user.getMem_pw())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        // 로그인 성공 처리 (예: 세션 생성, 토큰 발급 등)
        return true;
    }

}