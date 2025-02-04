package com.example.CineHive.entity.video;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Topmovie")
public class TopMovie {
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

}
