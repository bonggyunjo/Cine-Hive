package com.example.CineHive.service.creditService.movie;

import com.example.CineHive.entity.credit.movie.Video;
import com.example.CineHive.entity.videotype.Movie;
import com.example.CineHive.entity.videotype.TopMovie;
import com.example.CineHive.repository.videos.movie.MovieRepository;
import com.example.CineHive.repository.videos.movie.TopMovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TopRatedMovieService {
    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private MovieDirectorService movieDirectorService;
    @Autowired
    private MovieActorService movieActorService;
    @Autowired
    private MovieVideoService movieVideoService;
    @Autowired
    private TopMovieRepository topMovieRepository;
    @Autowired
    private MovieRepository movieRepository;

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
                    String releaseDateString = movieNode.get("release_date").asText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate releaseDate = LocalDate.parse(releaseDateString, formatter);
                    topMovie.setReleaseDate(releaseDate);
                    topMovies.add(topMovie);

                    // TopMovie 테이블에 저장
                    if (!topMovieRepository.existsById(movieId)) {
                        topMovieRepository.save(topMovie);  // topMovie 저장
                        System.out.println("Saved top movie: " + topMovie.getTitle());
                    } else {
                        System.out.println("Top movie already exists: " + topMovie.getTitle());
                    }

                    // Movie 테이블에 저장
                    if (!movieRepository.existsById(movieId)) {
                        Movie movie = new Movie();
                        movie.setId(movieId);
                        movie.setTitle(topMovie.getTitle());
                        movie.setOverview(topMovie.getOverview());
                        movie.setPosterPath(topMovie.getPosterPath());
                        movie.setBackdropPath(topMovie.getBackdropPath());
                        movie.setVoteAverage(topMovie.getVoteAverage());
                        movie.setVoteCount(topMovie.getVoteCount());
                        movie.setPopularity(topMovie.getPopularity());
                        movie.setReleaseDate(topMovie.getReleaseDate());
                        // 비디오 정보 가져오기 (첫 번째 비디오만)
                        Video video = movieVideoService.getFirstVideoForMovie(movieId);
                        if (video != null) {
                            movie.setVideos(List.of(video)); // 비디오 정보를 리스트로 설정
                        } else {
                            movie.setVideos(new ArrayList<>()); // 비디오가 없으면 빈 리스트 설정
                        }

                        // 필요한 추가 속성 설정
                        movieRepository.save(movie);  // Movie 저장
                        System.out.println("Saved movie: " + movie.getTitle());
                    } else {
                        System.out.println("Movie already exists: " + topMovie.getTitle());
                    }
                    // 배우 정보
                    movieActorService.saveMovieCredits(movieId);
                    // 감독 정보
                    movieDirectorService.saveMovieDirectors(movieId);
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
