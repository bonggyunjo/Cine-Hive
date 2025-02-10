package com.example.CineHive.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String memUserid;
    private String memPassword;

    // 기본 생성자
    public LoginDto() {}

    // 생성자
    public LoginDto(String memUserid, String memPassword) {
        this.memUserid = memUserid;
        this.memPassword = memPassword;
    }
}