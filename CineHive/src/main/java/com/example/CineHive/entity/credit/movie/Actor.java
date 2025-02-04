package com.example.CineHive.entity.credit.movie;

import com.example.CineHive.entity.video.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "actors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String originalName;
    private String role;
    private Integer gender;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
}
