package com.example.CineHive.service.creditService.animation;

import com.example.CineHive.entity.video.Animation;
import com.example.CineHive.repository.videos.animation.AnimationRepository;
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

    @Autowired
    private AnimationDirectorService animationDirectorService;

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
                    // 애니메이션만
                    if (!genreIds.contains(16)) {
                        continue;
                    }
                    Long animationId = animationNode.get("id").asLong();
                    String posterPath = animationNode.get("poster_path").asText();

                    // 포스터 이미지가 없으면 다음 데이터로 넘어감
                    if (posterPath == null || posterPath.isEmpty()) {
                        continue;
                    }
                    Animation animation = new Animation();
                    animation.setId(animationId); // movieId를 animationId로 변경
                    animation.setName(animationNode.get("title").asText());
                    animation.setOverview(animationNode.get("overview").asText());

                    animation.setPosterPath(posterPath);
                    animation.setBackdropPath(animationNode.get("backdrop_path").asText());
                    animation.setGenreIds(objectMapper.convertValue(animationNode.get("genre_ids"), List.class));  // List로 변환
                    animation.setVoteAverage(animationNode.get("vote_average").asDouble());
                    animation.setVoteCount(animationNode.get("vote_count").asInt());
                    animation.setPopularity(animationNode.get("popularity").asDouble());

                    if (!animationRepository.existsById(animationId)) {
                        animationRepository.save(animation);
                        System.out.println("Saved new animation: " + animation.getName());
                    } else {
                        System.out.println("Animation already exists: " + animation.getName());
                    }

                    animationDirectorService.saveAnimationDirectors(animationId);
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
