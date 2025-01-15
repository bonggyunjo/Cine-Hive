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
                                "/api/auth/google/success",
                                "/register"
                        ).permitAll()
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .sessionManagement(session -> session
                        .sessionFixation().newSession()
                        .maximumSessions(1).maxSessionsPreventsLogin(true)
                );

        return http.build();
    }    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // 클라이언트의 출처를 설정
        config.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드 설정
        source.registerCorsConfiguration("/**", config);
        return new FilterRegistrationBean<>(new CorsFilter(source));
    }
}