package com.example.CineHive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "actors")
@NoArgsConstructor
@AllArgsConstructor
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 배우 이름
    private String originalName; // 원래 이름
    private String character; // 배역 이름
    private Integer gender; // 성별 (0: 미정, 1: 여성, 2: 남성)

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie; // 영화와의 관계
}
