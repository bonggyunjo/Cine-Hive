package com.example.CineHive.dto.oauthInfo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo {
    private String googleId;

    private String email;

    private String nickname;

}
