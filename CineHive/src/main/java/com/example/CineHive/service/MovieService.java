package com.example.CineHive.service;

import com.example.CineHive.entity.Movie;
import com.example.CineHive.entity.PMovie;
import com.example.CineHive.repository.MovieRepository;
import com.example.CineHive.repository.PMovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final String apiKey = "d119fb469de12ae18b9fd948a485c7db";
    private final String Bearer_key="eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkMTE5ZmI0NjlkZTEyYWUxOGI5ZmQ5NDhhNDg1YzdkYiIsIm5iZiI6MTczMzQ5MTMxMC45ODQsInN1YiI6IjY3NTJmYTZlODBlNWI4ZjBhNzU2M2NiZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JoaKcxfdBOrUFiPGq8z3OkfKEJvQhBtyUJFOiP4_WIk";

    private final WebClient webClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PMovieRepository pmovieRepository;
    private final ObjectMapper objectMapper;


    @Autowired
    private MovieActorService movieActorService;

    public MovieService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
        this.objectMapper = objectMapper;
    }


    public Mono<String> getMoviesFromTMDB() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/movie/popular")
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
    @Transactional
    public void saveMoviesToDatabase() {
        String response = webClient.get()
                .uri("https://api.themoviedb.org/3/movie/now_playing?language=" + "ko" + "&page=" + "1" + "&api_key=" + apiKey)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();  // block()을 사용하여 응답을 기다립니다.

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode moviesNode = rootNode.path("results");
                JsonNode datesNode = rootNode.path("dates");

                for (JsonNode movieNode : moviesNode) {
                    Long movieId = movieNode.get("id").asLong();

                    // 영화가 이미 존재하는지 확인
                    if (!movieRepository.existsById(movieId)) {
                        Movie movie = new Movie();
                        movie.setId(movieId);
                        movie.setTitle(movieNode.get("title").asText());
                        String overviewText = movieNode.get("overview").asText();
                        movie.setOverview(overviewText);
                        movie.setPosterPath(movieNode.get("poster_path").asText());
                        movie.setReleaseDate(movieNode.get("release_date").asText());
                        movie.setBackdropPath(movieNode.get("backdrop_path").asText());
                        movie.setGenreIds(objectMapper.convertValue(movieNode.get("genre_ids"), List.class));  // List로 변환
                        movie.setVoteAverage(movieNode.get("vote_average").asDouble());
                        movie.setVoteCount(movieNode.get("vote_count").asInt());
                        movie.setPopularity(movieNode.get("popularity").asDouble());
                        movie.setAdult(movieNode.get("adult").asBoolean());

                        // Dates 필드 처리
                        Movie.Dates dates = new Movie.Dates();
                        dates.setMaximum(datesNode.path("maximum").asText());
                        dates.setMinimum(datesNode.path("minimum").asText());
                        movie.setDates(dates);

                        // 데이터베이스에 저장
                        movieRepository.save(movie);
                        System.out.println("Saved movie: " + movie.getTitle());

                        // 크레딧 정보 저장 호출
                        movieActorService.saveMovieCredits(movieId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
    }

    @Transactional
    public void saveTopRatedMoviesToDatabase() {
        String response = webClient.get()
                .uri("https://api.themoviedb.org/3/movie/top_rated?language=" + "ko" + "&page=" + "1" + "&api_key=" + apiKey)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();  // block()을 사용하여 응답을 기다립니다.

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode moviesNode = rootNode.path("results");
                JsonNode datesNode = rootNode.path("dates");

                for (JsonNode movieNode : moviesNode) {
                    Long movieId = movieNode.get("id").asLong();

                    // 영화가 이미 존재하는지 확인
                    if (!pmovieRepository.existsById(movieId)) {
                        PMovie movie = new PMovie();
                        movie.setId(movieId);
                        movie.setTitle(movieNode.get("title").asText());
                        String overviewText = movieNode.get("overview").asText();
                        movie.setOverview(overviewText);
                        movie.setPosterPath(movieNode.get("poster_path").asText());
                        movie.setReleaseDate(movieNode.get("release_date").asText());
                        movie.setBackdropPath(movieNode.get("backdrop_path").asText());
                        movie.setGenreIds(objectMapper.convertValue(movieNode.get("genre_ids"), List.class));  // List로 변환
                        movie.setVoteAverage(movieNode.get("vote_average").asDouble());
                        movie.setVoteCount(movieNode.get("vote_count").asInt());
                        movie.setPopularity(movieNode.get("popularity").asDouble());
                        movie.setAdult(movieNode.get("adult").asBoolean());

                        // Dates 필드 처리
                        PMovie.Dates dates = new PMovie.Dates();
                        dates.setMaximum(datesNode.path("maximum").asText());
                        dates.setMinimum(datesNode.path("minimum").asText());
                        movie.setDates(dates);

                        // 데이터베이스에 저장
                        pmovieRepository.save(movie);
                        System.out.println("Saved movie: " + movie.getTitle());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
    }


    public List<Movie> searchMovies(String query) {
        String response = webClient.get()
                .uri("https://api.themoviedb.org/3/search/movie?query="
                        + UriUtils.encode(query, StandardCharsets.UTF_8)
                        + "&api_key=" + apiKey
                        + "&include_adult=true&language=ko&page=1")
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();  // block()을 사용하여 응답을 기다립니다.

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
                    movie.setReleaseDate(movieNode.get("release_date").asText());
                    movie.setBackdropPath(movieNode.get("backdrop_path").asText());
                    movie.setGenreIds(objectMapper.convertValue(movieNode.get("genre_ids"), List.class));  // List로 변환
                    movie.setVoteAverage(movieNode.get("vote_average").asDouble());
                    movie.setVoteCount(movieNode.get("vote_count").asInt());
                    movie.setPopularity(movieNode.get("popularity").asDouble());
                    movie.setAdult(movieNode.get("adult").asBoolean());

                    // 영화가 데이터베이스에 존재하지 않으면 저장
                    if (!movieRepository.existsById(movieId)) {
                        movieRepository.save(movie);
                        System.out.println("Saved new movie: " + movie.getTitle());
                        movieActorService.saveMovieCredits(movieId);
                    } else {
                        System.out.println("Movie already exists: " + movie.getTitle());
                    }

                    // 데이터베이스와 상관없이 항상 리스트에 추가
                    movies.add(movie);
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