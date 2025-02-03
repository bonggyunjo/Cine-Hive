package com.example.CineHive.service;

import com.example.CineHive.entity.Video.Drama;
import com.example.CineHive.entity.Video.Movie;
import com.example.CineHive.entity.Video.TopMovie;
import com.example.CineHive.entity.credit.Video;
import com.example.CineHive.repository.Videos.DramaRepository;
import com.example.CineHive.repository.Videos.MovieRepository;
import com.example.CineHive.repository.Videos.TopMovieRepository;
import com.example.CineHive.service.movieCreditService.MovieActorService;
import com.example.CineHive.service.movieCreditService.MovieDirectorService;
import com.example.CineHive.service.movieCreditService.MovieVideoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;


import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TopMovieRepository topmovieRepository;
    private final ObjectMapper objectMapper;


    @Autowired
    private MovieActorService movieActorService;
    @Autowired
    private MovieVideoService movieVideoService;
    @Autowired
    private MovieDirectorService movieDirectorService;
    @Autowired
    private DramaRepository dramaRepository;


    public MovieService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
        this.objectMapper = objectMapper;
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
                        movie.setBackdropPath(movieNode.get("backdrop_path").asText());
                        movie.setGenreIds(objectMapper.convertValue(movieNode.get("genre_ids"), List.class));  // List로 변환
                        movie.setVoteAverage(movieNode.get("vote_average").asDouble());
                        movie.setVoteCount(movieNode.get("vote_count").asInt());
                        movie.setPopularity(movieNode.get("popularity").asDouble());
                        movie.setAdult(movieNode.get("adult").asBoolean());

                        // 비디오 정보 가져오기 (첫 번째 비디오만)
                        Video video = movieVideoService.getFirstVideoForMovie(movieId);
                        if (video != null) {
                            movie.setVideos(List.of(video)); // 비디오 정보를 리스트로 설정
                        }
                        // 데이터베이스에 저장
                        movieRepository.save(movie);
                        System.out.println("Saved movie: " + movie.getTitle());

                        // 배우 정보 저장
                        movieActorService.saveMovieCredits(movieId);
                        //감독 정보 저장
                        movieDirectorService.saveMovieDirectors(movieId);

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


                for (JsonNode movieNode : moviesNode) {
                    Long movieId = movieNode.get("id").asLong();

                    // 영화가 이미 존재하는지 확인
                    if (!topmovieRepository.existsById(movieId)) {
                        TopMovie movie = new TopMovie();
                        movie.setId(movieId);
                        movie.setTitle(movieNode.get("title").asText());
                        String overviewText = movieNode.get("overview").asText();
                        movie.setOverview(overviewText);
                        movie.setPosterPath(movieNode.get("poster_path").asText());
                        movie.setBackdropPath(movieNode.get("backdrop_path").asText());
                        movie.setGenreIds(objectMapper.convertValue(movieNode.get("genre_ids"), List.class));  // List로 변환
                        movie.setVoteAverage(movieNode.get("vote_average").asDouble());
                        movie.setVoteCount(movieNode.get("vote_count").asInt());
                        movie.setPopularity(movieNode.get("popularity").asDouble());
                        movie.setAdult(movieNode.get("adult").asBoolean());



                        // 데이터베이스에 저장
                        topmovieRepository.save(movie);
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
                    String posterPath = movieNode.get("poster_path").asText();

                    if(posterPath==null || posterPath.isEmpty()){
                        continue;
                    }
                    Movie movie = new Movie();
                    movie.setId(movieId);
                    movie.setTitle(movieNode.get("title").asText());
                    movie.setOverview(movieNode.get("overview").asText());
                    movie.setPosterPath(posterPath);

                    movie.setBackdropPath(movieNode.get("backdrop_path").asText());
                    movie.setGenreIds(objectMapper.convertValue(movieNode.get("genre_ids"), List.class));  // List로 변환
                    movie.setVoteAverage(movieNode.get("vote_average").asDouble());
                    movie.setVoteCount(movieNode.get("vote_count").asInt());
                    movie.setPopularity(movieNode.get("popularity").asDouble());
                    movie.setAdult(movieNode.get("adult").asBoolean());

                    // 비디오 정보 가져오기 (첫 번째 비디오만)
                    Video video = movieVideoService.getFirstVideoForMovie(movieId);
                    if (video != null) {
                        movie.setVideos(List.of(video)); // 비디오 정보를 리스트로 설정
                    } else {
                        movie.setVideos(new ArrayList<>()); // 비디오가 없으면 빈 리스트 설정
                    }

                    // 영화가 데이터베이스에 존재하지 않으면 저장
                    if (!movieRepository.existsById(movieId)) {
                        movieRepository.save(movie);
                        System.out.println("Saved new movie: " + movie.getTitle());
                        //배우 정보
                        movieActorService.saveMovieCredits(movieId);
                        //감독 정보
                        movieDirectorService.saveMovieDirectors(movieId);

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


    public List<Drama> searchDramas(String query) {
        String response = webClient.get()
                .uri("https://api.themoviedb.org/3/search/tv?query="
                        + UriUtils.encode(query, StandardCharsets.UTF_8)
                        + "&api_key=" + apiKey
                        + "&include_adult=true&language=ko&page=1")
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();  // block()을 사용하여 응답을 기다립니다.

        List<Drama> dramas = new ArrayList<>();
        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode dramasNode = rootNode.path("results");

                for (JsonNode dramaNode : dramasNode) {
                    Long dramaId = dramaNode.get("id").asLong();
                    String posterPath = dramaNode.get("poster_path").asText();
                    Drama drama = new Drama();
                    drama.setId(dramaId);
                    drama.setName(dramaNode.get("name").asText());
                    drama.setOverview(dramaNode.get("overview").asText());

                    // 포스터 이미지가 없으면 다음 데이터로 넘어감
                    if(posterPath==null || posterPath.isEmpty()){
                        continue;
                    }
                    drama.setPosterPath(posterPath);

                    drama.setBackdropPath(dramaNode.get("backdrop_path").asText());
                    drama.setGenreIds(objectMapper.convertValue(dramaNode.get("genre_ids"), List.class));  // List로 변환
                    drama.setVoteAverage(dramaNode.get("vote_average").asDouble());
                    drama.setVoteCount(dramaNode.get("vote_count").asInt());
                    drama.setPopularity(dramaNode.get("popularity").asDouble());
                    drama.setAdult(dramaNode.get("adult").asBoolean());


                    if (!dramaRepository.existsById(dramaId)) {
                        dramaRepository.save(drama);
                        System.out.println("Saved new movie: " + drama.getName());

                    } else {
                        System.out.println("Drama already exists: " + drama.getName());
                    }

                    // 데이터베이스와 상관없이 항상 리스트에 추가
                    dramas.add(drama);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
        return dramas;
    }
}