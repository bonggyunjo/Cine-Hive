package com.example.CineHive.controller;

import com.example.CineHive.entity.Movie;
import com.example.CineHive.repository.MovieRepository;
import com.example.CineHive.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/now_playing")
    public ResponseEntity<?> getNowPlayingMovies() {
        System.out.println("Request received for now playing movies");
        movieService.saveMoviesToDatabase();  // 매개변수로 language와 page 전달
        return ResponseEntity.ok().body("성공적으로 데이터를 저장했습니다!");
    }

    @GetMapping("/movies")
    @ResponseBody
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
