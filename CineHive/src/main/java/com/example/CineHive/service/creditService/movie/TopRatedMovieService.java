package com.example.CineHive.service.creditService.movie;

import com.example.CineHive.entity.videotype.TopMovie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopRatedMovieService {
    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public TopRatedMovieService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
        this.objectMapper = objectMapper;
    }


    public List<TopMovie> getTopRatedMovies(Pageable pageable) {
        String response = webClient.get()
                .uri("https://api.themoviedb.org/3/movie/top_rated?language=ko&page=" + (pageable.getPageNumber() + 1) + "&api_key=" + apiKey)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<TopMovie> topMovies = new ArrayList<>();
        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode moviesNode = rootNode.path("results");

                for (JsonNode movieNode : moviesNode) {
                    Long movieId = movieNode.get("id").asLong();
                    TopMovie topMovie = new TopMovie();
                    topMovie.setId(movieId);
                    topMovie.setTitle(movieNode.get("title").asText());
                    topMovie.setOverview(movieNode.get("overview").asText());
                    topMovie.setPosterPath(movieNode.get("poster_path").asText());
                    topMovie.setBackdropPath(movieNode.get("backdrop_path").asText());
                    topMovie.setVoteAverage(movieNode.get("vote_average").asDouble());
                    topMovie.setVoteCount(movieNode.get("vote_count").asInt());
                    topMovie.setPopularity(movieNode.get("popularity").asDouble());
                    topMovies.add(topMovie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
        return topMovies;
    }
}
