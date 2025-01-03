package com.example.CineHive.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mem_id;

    @Column(nullable = false)
    private String mem_userid;

    @Column
    private String mem_pw;

    @Column(nullable = false)
    private String mem_email;

    @Column
    private String mem_name;

    @Column(nullable = false)
    private String mem_nickname;

    @Column
    private String mem_sex;

    @Column
    private String mem_phone;

    @Column(nullable = false)
    private LocalDateTime mem_register_datetime;

    @Column(nullable = false)
    private int mem_gener;

    @Column(nullable = false)
    private String mem_type;

    @PrePersist //필드를 자동으로 현재 시간으로 설정
    public void prePersist() {
        this.mem_register_datetime = LocalDateTime.now();
    }
}
