package com.example.CineHive.controller;


import com.example.CineHive.dto.Content;
import com.example.CineHive.dto.PreferredGenereDto;
import com.example.CineHive.entity.videotype.Animation;
import com.example.CineHive.entity.videotype.Drama;
import com.example.CineHive.entity.videotype.Movie;
import com.example.CineHive.repository.videos.animation.AnimationRepository;
import com.example.CineHive.repository.videos.drama.DramaRepository;
import com.example.CineHive.repository.videos.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PreferredGenreController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private DramaRepository dramaRepository;
    @Autowired
    private AnimationRepository animationRepository;

    @PostMapping("/preferredGenres")
    public ResponseEntity<List<Content>> getContentByGenres(@RequestBody PreferredGenereDto preferredGenereDto) {
        List<Content> contents = new ArrayList<>();

        for (String genre : preferredGenereDto.getGeners()) {
            switch (genre) {
                case "드라마":
                    for (Drama drama : dramaRepository.findAll()) {
                        contents.add(convertToContent(drama));
                    }
                    break;
                case "애니메이션":
                    for (Animation animation : animationRepository.findAll()) {
                        contents.add(convertToContent(animation));
                    }
                    break;
                case "영화":
                    for (Movie movie : movieRepository.findAll()) {
                        contents.add(convertToContent(movie));
                    }
                    break;
            }
        }

        return ResponseEntity.ok(contents);
    }
    private Content convertToContent(Drama drama) {
        return new Content(drama.getId(), drama.getName(), drama.getOverview(), drama.getPosterPath());
    }

    private Content convertToContent(Animation animation) {
        return new Content(animation.getId(), animation.getName(), animation.getOverview(), animation.getPosterPath());
    }

    private Content convertToContent(Movie movie) {
        return new Content(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath());
    }
}
