package com.example.CineHive.controller;

import com.example.CineHive.entity.Video.Animation;
import com.example.CineHive.entity.Video.Drama;
import com.example.CineHive.entity.Video.Movie;
import com.example.CineHive.repository.Videos.DramaRepository;
import com.example.CineHive.repository.Videos.MovieRepository;
import com.example.CineHive.service.AnimationService;
import com.example.CineHive.service.DramaService;
import com.example.CineHive.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private DramaService dramaService;
    @Autowired
    private AnimationService animationService;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private DramaRepository dramaRepository;


    @GetMapping("/now_playing")
    public ResponseEntity<?> getNowPlayingMovies() {
        System.out.println("Request received for now playing movies");
        movieService.saveMoviesToDatabase();  // 매개변수로 language와 page 전달
        return ResponseEntity.ok().body("성공적으로 데이터를 저장했습니다!");
    }

    //데이터베이스에 있는 영화를 그냥 다 가져오는 것(homepage.vue 열면 바로 실행)
    @GetMapping("/movies")
    @ResponseBody
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/popular_movie")
    public ResponseEntity<?> getPopularMovies() {
        System.out.println("Request received for Top movies");
        movieService.saveTopRatedMoviesToDatabase();  // 매개변수로 language와 page 전달
        return ResponseEntity.ok().body("성공적으로 데이터를 저장했습니다!");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam String query) {
        System.out.println("Request received for searching movies");
        List<Movie> searchResults1 = movieService.searchMovies(query);  // 검색 결과 받기
        List<Drama> searchResults2 = dramaService.searchDramas(query);  // 검색 결과 받기
        List<Animation> searchResults3 = animationService.searchAnimations(query);  // 검색 결과 받기

        Map<String,Object> response = new HashMap<>();
        response.put("movies", searchResults1);
        response.put("dramas", searchResults2);
        response.put("animations", searchResults3);

        return ResponseEntity.ok().body(response);  // 검색 결과를 클라이언트로 반환
    }



    @GetMapping("/movies/{id}")
    @ResponseBody
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            return ResponseEntity.ok(movieOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dramas/{id}")
    @ResponseBody
    public ResponseEntity<Drama> getDramaById(@PathVariable Long id) {
        Optional<Drama> dramaOptional = dramaRepository.findById(id);
        if (dramaOptional.isPresent()) {
            return ResponseEntity.ok(dramaOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
