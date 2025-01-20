package com.example.CineHive.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/movies",
                                         "/now_playing",
                                         "/search",
                                         "/popular_movie").permitAll()
                        .requestMatchers("/login", "/register", "/checkuserId/**","/checknickname/**","/checkemail/**",
                                "/api/auth/kakao/check-user","/api/auth/kakao/register",
                                "/api/auth/google/register","/api/auth/google/check-user","/api/auth/naver/check-user","/api/auth/naver/register").permitAll() // 로그인과 회원가입은 누구나 접근 가능
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
                                "/login"
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