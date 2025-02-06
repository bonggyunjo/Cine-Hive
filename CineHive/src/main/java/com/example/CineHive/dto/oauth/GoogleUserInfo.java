package com.example.CineHive.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo {
    private String googleId;
    private String email;
    private String nickname;

    // 추가된 필드들
    private String name;  // 사용자의 이름
    private List<String> genres;  // 사용자가 선택한 장르 리스트
}
