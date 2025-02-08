package com.example.CineHive.service.creditService.movie;

import com.example.CineHive.entity.credit.movie.Video;
import com.example.CineHive.entity.videotype.Movie;
import com.example.CineHive.repository.videos.movie.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NowPlayingMovieService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieDirectorService movieDirectorService;
    @Autowired
    private MovieActorService movieActorService;
    @Autowired
    private MovieVideoService movieVideoService;

    public NowPlayingMovieService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
        this.objectMapper = objectMapper;
    }

    public List<Movie> getNowPlayingMovies(Pageable pageable) {
        String response = webClient.get()
                .uri("https://api.themoviedb.org/3/movie/now_playing?language=ko&page=" + (pageable.getPageNumber() + 1) + "&api_key=" + apiKey)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<Movie> movies = new ArrayList<>();
        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode moviesNode = rootNode.path("results");

                for (JsonNode movieNode : moviesNode) {
                    Long movieId = movieNode.get("id").asLong();
                    Movie movie = new Movie();
                    movie.setId(movieId);
                    movie.setTitle(movieNode.get("title").asText());
                    movie.setOverview(movieNode.get("overview").asText());
                    movie.setPosterPath(movieNode.get("poster_path").asText());
                    movie.setBackdropPath(movieNode.get("backdrop_path").asText());
                    movie.setVoteAverage(movieNode.get("vote_average").asDouble());
                    movie.setVoteCount(movieNode.get("vote_count").asInt());
                    movie.setPopularity(movieNode.get("popularity").asDouble());
                    String releaseDateString = movieNode.get("release_date").asText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate releaseDate = LocalDate.parse(releaseDateString, formatter);
                    movie.setReleaseDate(releaseDate);
                    movies.add(movie);

                    // 비디오 정보 가져오기 (첫 번째 비디오만)
                    Video video = movieVideoService.getFirstVideoForMovie(movieId);
                    if (video != null) {
                        movie.setVideos(List.of(video)); // 비디오 정보를 리스트로 설정
                    } else {
                        movie.setVideos(new ArrayList<>()); // 비디오가 없으면 빈 리스트 설정
                    }
                    // 데이터베이스에 저장
                    if (!movieRepository.existsById(movieId)) {
                        movieRepository.save(movie);  // 영화 저장
                        System.out.println("Saved movie: " + movie.getTitle());
                    } else {
                        System.out.println("Movie already exists: " + movie.getTitle());
                    }
                    //배우 정보
                    movieActorService.saveMovieCredits(movieId);
                    //감독 정보
                    movieDirectorService.saveMovieDirectors(movieId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
        return movies;
    }
}
