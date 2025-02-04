package com.example.CineHive.dto.oauth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {

    private String kakaoId;
    private String email;
    private String nickname;
}
