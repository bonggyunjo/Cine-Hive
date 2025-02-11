package com.example.CineHive.controller;

import com.example.CineHive.entity.videotype.Animation;
import com.example.CineHive.entity.videotype.Drama;
import com.example.CineHive.entity.videotype.Movie;
import com.example.CineHive.entity.videotype.TopMovie;
import com.example.CineHive.repository.videos.movie.MovieRepository;
import com.example.CineHive.repository.videos.movie.TopMovieRepository;
import com.example.CineHive.service.creditService.animation.AnimationService;
import com.example.CineHive.service.creditService.drama.DramaService;
import com.example.CineHive.service.creditService.movie.MovieService;
import com.example.CineHive.service.creditService.movie.NowPlayingMovieService;
import com.example.CineHive.service.creditService.movie.TopRatedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private TopMovieRepository topmovieRepository;

    @Autowired
    private NowPlayingMovieService nowPlayingMovieService;

    @Autowired
    private TopRatedMovieService topRatedMovieService;
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

    //Topmovie 데이블에서 가져오기
    @GetMapping("/get_topmovies")
    @ResponseBody
    public ResponseEntity<List<TopMovie>> getTopRatedMoviesList() {
        Pageable pageable = PageRequest.of(0, 22); // 첫 번째 페이지에서 22개 가져오기
        List<TopMovie> topRatedMovies = topRatedMovieService.getTopRatedMovies(pageable);
        return ResponseEntity.ok(topRatedMovies);
    }
    //TopRated 영화 DB에 넣기
    @GetMapping("/top_movie")
    public ResponseEntity<?> getTopMovies() {
        System.out.println("Request received for Top movies");
        movieService.saveTopRatedMoviesToDatabase();
        return ResponseEntity.ok().body("성공적으로 데이터를 저장했습니다!");
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestBody Map<String, String> request) {
        String query = request.get("query");

        System.out.println("Request received for searching movies: " + query);
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
    
    @GetMapping("/now_playing_movies")
    @ResponseBody
    public ResponseEntity<List<Movie>> getNowPlayingMoviesList() {
        Pageable pageable = PageRequest.of(0, 22); // 첫 번째 페이지에서 22개 가져오기
        List<Movie> nowPlayingMovies = nowPlayingMovieService.getNowPlayingMovies(pageable);
        return ResponseEntity.ok(nowPlayingMovies);
    }
}
