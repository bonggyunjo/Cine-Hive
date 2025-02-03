package com.example.CineHive.service;

import com.example.CineHive.entity.Video.Animation;
import com.example.CineHive.entity.Video.Drama;
import com.example.CineHive.repository.Videos.AnimationRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnimationService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;
    @Autowired
    private AnimationRepository animationRepository;
    private final ObjectMapper objectMapper;


    public AnimationService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
        this.objectMapper = objectMapper;
    }
    public List<Animation> searchAnimations(String query) {
        String response = webClient.get()
                .uri("https://api.themoviedb.org/3/search/movie?query="
                        + UriUtils.encode(query, StandardCharsets.UTF_8)
                        + "&api_key=" + apiKey
                        + "&include_adult=true&language=ko&page=1")
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block();  // block()을 사용하여 응답을 기다립니다.

        List<Animation> animations = new ArrayList<>();

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode animationsNode = rootNode.path("results");

                for (JsonNode animationNode : animationsNode) {
                    List<Integer> genreIds = objectMapper.convertValue(animationNode.get("genre_ids"), List.class);
                    //애니매이션만
                    if(!genreIds.contains(16)){
                        continue;
                    }
                    Long dramaId = animationNode.get("id").asLong();
                    String posterPath = animationNode.get("poster_path").asText();

                    // 포스터 이미지가 없으면 다음 데이터로 넘어감
                    if(posterPath==null || posterPath.isEmpty()){
                        continue;
                    }
                    Animation animation = new Animation();
                    animation.setId(dramaId);
                    animation.setName(animationNode.get("title").asText());
                    animation.setOverview(animationNode.get("overview").asText());


                    animation.setPosterPath(posterPath);

                    animation.setBackdropPath(animationNode.get("backdrop_path").asText());
                    animation.setGenreIds(objectMapper.convertValue(animationNode.get("genre_ids"), List.class));  // List로 변환
                    animation.setVoteAverage(animationNode.get("vote_average").asDouble());
                    animation.setVoteCount(animationNode.get("vote_count").asInt());
                    animation.setPopularity(animationNode.get("popularity").asDouble());


                    if (!animationRepository.existsById(dramaId)) {
                        animationRepository.save(animation);
                        System.out.println("Saved new animatinon: " + animation.getName());

                    } else {
                        System.out.println("animatinon already exists: " + animation.getName());
                    }

                    // 데이터베이스와 상관없이 항상 리스트에 추가
                    animations.add(animation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("응답이 없습니다.");
        }
        return animations;
    }

}
