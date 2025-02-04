package com.example.CineHive.entity.video;

import com.example.CineHive.entity.credit.animation.Director;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animation")
public class Animation {

    @Id
    private Long id;
    private String name;

    @Lob
    private String overview;
    private String posterPath;
    private String backdropPath;

    private double voteAverage;
    private int voteCount;
    private double popularity;

    @ElementCollection
    private List<Integer> genreIds;

    @OneToMany(mappedBy = "animation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Director> directors = new ArrayList<>(); // 빈 리스트로 초기화

}
