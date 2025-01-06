package com.example.CineHive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NaverUserInfo {
    private String id;

    private String naverId;

    private String email;

    private String nickname;

    private String name;

    private String gender;

    private String phone;
}
