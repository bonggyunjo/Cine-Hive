package com.example.CineHive.entity.credit.animation;

import com.example.CineHive.entity.video.Animation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animation_vidoes")
@Entity(name="AnimationVideo")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoKey; // 유튜브 비디오 키
    private String name;
    private String site;
    private String type; // type은 트레일러 부분

    @ManyToOne // 다대일 관계 설정
    @JoinColumn(name = "animation_id") // 외래 키 설정
    private Animation animation; // Animation과의 관계 추가
}
