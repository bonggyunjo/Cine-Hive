package com.example.CineHive.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String mem_userid;
    private String mem_password;

    // 기본 생성자
    public LoginRequest() {}

    // 생성자
    public LoginRequest(String mem_userid, String mem_password) {
        this.mem_userid = mem_userid;
        this.mem_password = mem_password;
    }
}