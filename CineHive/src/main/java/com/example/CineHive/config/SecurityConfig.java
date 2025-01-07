package com.example.CineHive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/*
코드는 소셜 로그인에 대한 권한 설정 관련 코드
*/

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/api/auth/kakao",
                                "/api/auth/logout",
                                "/api/auth/kakao/callback",
                                "/api/auth/kakao/success",
                                "/api/auth/session",
                                "/api/auth/naver",
                                "/api/auth/naver/callback",
                                "/api/auth/naver/success",
                                "/api/auth/google",
                                "/api/auth/google/callback",
                                "/api/auth/google/success"
                        ).permitAll() // 카카오 URL 허용
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // 기본 로그인 페이지 설정
                        .defaultSuccessUrl("/success") // 로그인 성공 후 리다이렉트 URL
                        .failureUrl("/api/auth/failure") // 로그인 실패 시 리다이렉트 URL
                )
                .sessionManagement(session -> session
                        .sessionFixation().newSession() // 세션 고정 공격 방지
                        .maximumSessions(1).maxSessionsPreventsLogin(true) // 최대 세션 수 설정
                );

        return http.build();
    }
}