package com.example.CineHive.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String memUserid;
    private String memPassword;

    // 기본 생성자
    public LoginRequest() {}

    // 생성자
    public LoginRequest(String memUserid, String memPassword) {
        this.memUserid = memUserid;
        this.memPassword = memPassword;
    }
}