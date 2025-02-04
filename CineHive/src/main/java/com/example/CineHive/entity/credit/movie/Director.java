package com.example.CineHive.entity.credit.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "MovieDirector") // 엔티티 이름 변경
@Table(name = "movie_directors")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    private String name; // 감독 이름
    
    private int gender;

    private String job;
}
