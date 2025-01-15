package com.example.CineHive.service;

import com.example.CineHive.dto.UserDto;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public boolean registerUser(UserDto userDto) {
        User user= new User();
        user.setMemEmail(userDto.getMemEmail());
        user.setMemUserid(userDto.getMemUserid());
        user.setMemPw(passwordEncoder.encode(userDto.getMemPassword()));
        user.setMemName(userDto.getMemName());
        user.setMemSex(userDto.getMemSex());
        user.setMemPhone(userDto.getMemPhone());
        user.setMemNickname(userDto.getMemNickname());
        user.setMemRegisterDatetime(LocalDateTime.now());
        user.setGenres(userDto.getGenres());
        user.setMemType("일반");
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

    public boolean loginUser(String memUserid, String memPassword) {
        // 사용자 ID로 사용자 조회
        Optional<User> existingUser = userRepository.findByMemUserid(memUserid);

        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        User user = existingUser.get();

        // 비밀번호 비교
        if (!passwordEncoder.matches(memPassword, user.getMemPw())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        return true;
    }
    public boolean checkUserExists(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId).isPresent();
    }
    public boolean checkUserExistsGoogle(String googleId) {
        return userRepository.findByGoogleId(googleId).isPresent();
    }

    public boolean checkUserExistsNaver(String naverId) {
        return userRepository.findByNaverId(naverId).isPresent();
    }
}