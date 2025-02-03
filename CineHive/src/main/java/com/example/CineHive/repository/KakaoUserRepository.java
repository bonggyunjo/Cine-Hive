package com.example.CineHive.repository;

import com.example.CineHive.entity.oauthUser.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoUserRepository extends JpaRepository<KakaoUser, Long> {
    Optional<KakaoUser> findByKakaoId(String kakaoId);
}
