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
        // 전송된 선호 장르 로그 출력
        System.out.println("전송된 선호 장르: " + preferredGenereDto.getGeneres());

        List<Content> contents = new ArrayList<>();

        for (String genre : preferredGenereDto.getGeneres()) {
            switch (genre) {
                case "드라마":
                    for (Drama drama : dramaRepository.findAll()) {
                        contents.add(convertToContent(drama));
                        System.out.println("드라마 개수: " + dramaRepository.findAll().size());
                    }
                    break;
                case "애니메이션":
                    for (Animation animation : animationRepository.findAll()) {
                        contents.add(convertToContent(animation));
                        System.out.println("애니메이션 개수: " + animationRepository.findAll().size());
                    }
                    break;
                case "영화":
                    for (Movie movie : movieRepository.findAll()) {
                        contents.add(convertToContent(movie));
                        System.out.println("영화 개수: " + movieRepository.findAll().size());
                    }
                    break;
            }
        }


        if (contents.isEmpty()) {
            System.out.println("선호 장르에 대한 콘텐츠가 없습니다.");
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
