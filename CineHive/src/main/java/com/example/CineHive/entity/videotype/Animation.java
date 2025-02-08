package com.example.CineHive.entity.videotype;

import com.example.CineHive.entity.credit.animation.Director;
import com.example.CineHive.entity.credit.animation.Video;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    @Column(name = "release_date")
    private LocalDate releaseDate;
    private double voteAverage;
    private int voteCount;
    private double popularity;

    @ElementCollection
    private List<Integer> genreIds;

    @OneToMany(mappedBy = "animation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Director> directors = new ArrayList<>();


    @OneToMany(mappedBy = "animation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Video> videos = new ArrayList<>();
}
