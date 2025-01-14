package com.example.CineHive.service;

import com.example.CineHive.entity.Movie;
import com.example.CineHive.repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MovieService {
    private final String apiKey = "d119fb469de12ae18b9fd948a485c7db";

    private final WebClient webClient;

    @Autowired
    private MovieRepository movieRepository;
    private final ObjectMapper objectMapper;


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
                .uri("https://api.themoviedb.org/3/movie/now_playing?language=" + "ko" + "&page=" + "2" + "&api_key=" + apiKey)
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
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
    }
}
