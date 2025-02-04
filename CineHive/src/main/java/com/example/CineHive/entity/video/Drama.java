package com.example.CineHive.entity.video;

import com.example.CineHive.entity.credit.drama.Director;
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
@Table(name = "drama")
public class Drama {
    @Id
    private Long id;
    private String name;
    private String originalName;
    @Lob
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String firstAirDate;
    private double voteAverage;
    private int voteCount;
    private double popularity;
    private boolean adult;
    private String originalLanguage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "drama_id")
    private List<Director> directors;

    @ElementCollection
    private List<Integer> genreIds;

}
