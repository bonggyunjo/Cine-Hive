package com.example.CineHive.entity.credit.animation;

import com.example.CineHive.entity.video.Animation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animation_directors")
@Entity(name = "AnimationDirector")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "animation_id") // 외래 키 설정
    private Animation animation; // Animation과의 관계 추가
}
