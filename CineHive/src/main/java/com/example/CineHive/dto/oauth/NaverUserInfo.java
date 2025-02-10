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
public class NaverUserInfo {

    private String naverId;
    private String email;
    private String nickname;
    private String name;
    private List<String> genres;
}
