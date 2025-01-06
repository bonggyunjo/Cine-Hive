package com.example.CineHive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "naver_users")
public class NaverUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naver_id", unique = true)
    private String naverId; //네이버 고유 아이디

    @Column(length = 50)
    private String nickname; //별명

    @Column
    private String mem_name; //회원 이름

    @Column(name = "mem_userid", nullable = false)
    private String memUserId; //이메일

    @Column
    private String mem_sex;

    @Column
    private String mem_phone;

    @Column(name = "user_id")
    private Long userId; // users 테이블의 외래 키

    // User 엔티티와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "mem_id", insertable = false, updatable = false)
    private User user;

    public NaverUser(String naverId, String nickname, String mem_name, String memUserId, String mem_sex, String mem_phone) {
        this.naverId = naverId;
        this.nickname = nickname;
        this.mem_name = mem_name;
        this.memUserId = memUserId; // 추가된 필드 초기화
        this.mem_sex= mem_sex;
        this.mem_phone=mem_phone;
    }
}
