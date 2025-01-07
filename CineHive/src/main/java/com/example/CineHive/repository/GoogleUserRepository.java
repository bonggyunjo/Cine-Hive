package com.example.CineHive.repository;

import com.example.CineHive.entity.GoogleUser;
import com.example.CineHive.entity.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {
    Optional<GoogleUser> findByKakaoId(String googleId);
}
