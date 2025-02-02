package com.example.CineHive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

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

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Actor> actors; // 출연진
}
