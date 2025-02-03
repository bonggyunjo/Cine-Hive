package com.example.CineHive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopMovieDto {
    private int page;
    private List<TopMovieDTO> results;

    public static class TopMovieDTO {
        private long id;
        private String title;
        private String overview;
        private String posterPath;
        private String backdropPath;
        private String releaseDate;
        private List<Integer> genreIds;
        private double voteAverage;
        private int voteCount;
        private double popularity;
        private boolean adult;
        }
}
