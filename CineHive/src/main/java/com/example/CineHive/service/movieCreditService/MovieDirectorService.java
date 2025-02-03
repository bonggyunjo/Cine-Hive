package com.example.CineHive.service.movieCreditService;

import com.example.CineHive.entity.credit.Director;
import com.example.CineHive.entity.VideoType.Movie;
import com.example.CineHive.repository.DirectorRepository;
import com.example.CineHive.repository.Videos.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MovieDirectorService {
    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DirectorRepository directorRepository;

    public MovieDirectorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    @Transactional
    public void saveMovieDirectors(Long movieId) {
        String response = webClient.get()
                .uri("/movie/" + movieId + "/credits?api_key=" + apiKey + "&language=ko")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode crewNode = rootNode.path("crew");

                Movie movie = movieRepository.findById(movieId).orElse(null);
                if (movie != null) {
                    for (JsonNode crewMember : crewNode) {
                        // 감독 정보를 찾기 위해 "job" 속성이 "Director"인 경우만 필터링
                        if ("Director".equals(crewMember.get("job").asText())) {
                            Director director = new Director();
                            director.setName(crewMember.get("name").asText());
                            director.setGender(crewMember.get("gender").asInt());
                            director.setJob(crewMember.get("job").asText());

                            // Movie에 Director 추가
                            movie.setDirector(director);
                            break; // 감독은 한 명만 있으므로 루프 탈출
                        }
                    }
                    movieRepository.save(movie);
                }
            } catch (Exception e) {
                System.out.println("JSON 처리 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
    }

}
