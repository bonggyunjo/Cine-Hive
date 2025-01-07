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
public class GoogleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_id", unique = true)
    private String googleId;


    @Column(length = 50)
    private String nickname;

    @Column(name = "mem_userid", nullable = false)
    private String memUserId;

    @Column(name = "user_id")
    private Long userId; // users 테이블의 외래 키

    // User 엔티티와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "mem_id", insertable = false, updatable = false)
    private User user;

    public GoogleUser(String googleId, String nickname, String memUserId) {
        this.googleId = googleId;
        this.nickname = nickname;
        this.memUserId = memUserId; // 추가된 필드 초기화

    }
}

