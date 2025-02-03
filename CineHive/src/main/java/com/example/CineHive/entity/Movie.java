package com.example.CineHive.entity;

import com.example.CineHive.entity.credit.Actor;
import com.example.CineHive.entity.credit.Director;
import com.example.CineHive.entity.credit.Video;
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
@Table(name = "movie")
public class Movie {
    @Id
    private Long id;

    private String title;
    @Lob
    private String overview;

    private String posterPath;

    private String backdropPath;

    private String releaseDate;

    @ElementCollection
    private List<Integer> genreIds;

    private double voteAverage;

    private int voteCount;

    private double popularity;

    private boolean adult;

    @Embedded
    private Dates dates;

    // Dates embeddable class for nested dates information
    @Embeddable
    @Getter
    @Setter
    public static class Dates {
        private String maximum;
        private String minimum;
    }

    //출연진 정보
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actor> actors = new ArrayList<>();

    public void addActor(Actor actor) {
        actors.add(actor);
        actor.setMovie(this);
    }

    // 비디오 정보를 저장하는 리스트
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private List<Video> videos;


    // 감독 정보
    @ManyToOne(cascade = CascadeType.PERSIST) // 추가
    @JoinColumn(name = "director_id") // 외래 키
    private Director director;



}
