package com.example.CineHive.controller;

import com.example.CineHive.entity.Movie;
import com.example.CineHive.repository.MovieRepository;
import com.example.CineHive.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/popular_movie")
    public ResponseEntity<?> getPopularMovies() {
        System.out.println("Request received for popular movies");
        movieService.saveTopRatedMoviesToDatabase();  // 매개변수로 language와 page 전달
        return ResponseEntity.ok().body("성공적으로 데이터를 저장했습니다!");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam String query) {
        System.out.println("Request received for searching movies");
        List<Movie> searchResults = movieService.searchMovies(query);  // 검색 결과 받기
        return ResponseEntity.ok().body(searchResults);  // 검색 결과를 클라이언트로 반환
    }

    @GetMapping("/movies/{id}") //
    @ResponseBody
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            return ResponseEntity.ok(movieOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
