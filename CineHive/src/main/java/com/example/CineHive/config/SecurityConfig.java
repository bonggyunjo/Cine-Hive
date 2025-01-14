package com.example.CineHive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                .csrf(csrf -> csrf.disable())
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
                                "/api/auth/google/success",
                                "/register",
                                "/login",
                                "/now_playing"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionFixation().newSession()
                        .maximumSessions(1).maxSessionsPreventsLogin(true)
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCrpytPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}