package com.example.CineHive.entity.oauthUser;

import com.example.CineHive.entity.User;
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

    @Column(name = "mem_userid", nullable = false)
    private String memUserId; //이메일

    @Column(name = "user_id")
    private Long userId; // users 테이블의 외래 키

    // User 엔티티와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "mem_id", insertable = false, updatable = false)
    private User user;

    public NaverUser(String naverId, String nickname, String memUserId) {
        this.naverId = naverId;
        this.nickname = nickname;
        this.memUserId = memUserId; // 추가된 필드 초기화
    }
}
