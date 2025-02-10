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

        System.out.println("전송된 선호 장르: " + preferredGenereDto.getGenres());

        List<Content> contents = new ArrayList<>();

        for (String genre : preferredGenereDto.getGenres()) {
            switch (genre) {
                case "드라마":
                    List<Drama> dramas = dramaRepository.findAll();
                    for (Drama drama : dramas) {
                        contents.add(convertToContent(drama));
                        if (contents.size() >= 18) break;
                    }
                    break;
                case "애니메이션":
                    List<Animation> animations = animationRepository.findAll();
                    for (Animation animation : animations) {
                        contents.add(convertToContent(animation));
                        if (contents.size() >= 18) break;
                    }
                    break;
                case "영화":
                    List<Movie> movies = movieRepository.findAll();
                    for (Movie movie : movies) {
                        contents.add(convertToContent(movie));
                        if (contents.size() >= 18) break;
                    }
                    break;
            }
            if (contents.size() >= 18) break; // 모든 장르에서 18개 이상이면 종료
        }

        if (contents.isEmpty()) {
            System.out.println("선호 장르에 대한 콘텐츠가 없습니다.");
        }

        return ResponseEntity.ok(contents);
    }

    private Content convertToContent(Drama drama) {
        return new Content(drama.getId(), drama.getName(), drama.getOverview(), drama.getPosterPath(), "드라마");
    }

    private Content convertToContent(Animation animation) {
        return new Content(animation.getId(), animation.getName(), animation.getOverview(), animation.getPosterPath(), "애니메이션");
    }

    private Content convertToContent(Movie movie) {
        return new Content(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath(), "영화");
    }
}
