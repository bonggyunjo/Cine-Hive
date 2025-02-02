package com.example.CineHive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "actors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 배우 이름
    private String originalName; // 원래 이름
    private String role; // 캐릭터 이름으로 변경
    private Integer gender; // 성별 (0: 미정, 1: 여성, 2: 남성)

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie; // 영화와의 관계
}
