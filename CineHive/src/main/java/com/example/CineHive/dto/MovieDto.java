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
public class MovieDto {
    private DatesDTO dates;
    private int page;
    private List<MovieDTO> results;

    // DatesDTO inner class
    public static class DatesDTO {
        private String maximum;
        private String minimum;
    }
    public static class MovieDTO {
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
