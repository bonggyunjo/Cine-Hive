package com.example.CineHive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long mem_id;
    private String mem_userid;
    private String mem_password;
    private String mem_name;
    private String mem_email;
    private String mem_phone;
    private String mem_nickname;
    private String mem_sex;
    private String mem_type;
    private String mem_register_datetime;
    private int mem_gener;
}
