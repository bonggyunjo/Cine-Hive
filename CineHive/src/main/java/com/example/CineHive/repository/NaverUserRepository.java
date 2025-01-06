package com.example.CineHive.repository;

import com.example.CineHive.entity.KakaoUser;
import com.example.CineHive.entity.NaverUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaverUserRepository extends JpaRepository<NaverUser, Long> {
    Optional<NaverUser> findByNaverId(String naverId);
}
