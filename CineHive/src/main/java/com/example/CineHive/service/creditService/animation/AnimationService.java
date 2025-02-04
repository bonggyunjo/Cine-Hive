package com.example.CineHive.service.creditService.animation;

import com.example.CineHive.entity.credit.animation.Video;
import com.example.CineHive.entity.videotype.Animation;
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

    @Autowired
    private AnimationVideoService animationVideoService;

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
                .block();  // 응답을 기다림

        List<Animation> animations = new ArrayList<>();

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode animationsNode = rootNode.path("results");

                for (JsonNode animationNode : animationsNode) {
                    List<Integer> genreIds = objectMapper.convertValue(animationNode.get("genre_ids"), List.class);
                    // 애니메이션 장르(16)만 필터링
                    if (!genreIds.contains(16)) {
                        continue;
                    }
                    Long animationId = animationNode.get("id").asLong();
                    String posterPath = animationNode.get("poster_path").asText();

                    // 포스터 없는 경우 제외
                    if (posterPath == null || posterPath.isEmpty()) {
                        continue;
                    }

                    Animation animation = animationRepository.findById(animationId).orElse(new Animation());
                    animation.setId(animationId);
                    animation.setName(animationNode.get("title").asText());
                    animation.setOverview(animationNode.get("overview").asText());
                    animation.setPosterPath(posterPath);
                    animation.setBackdropPath(animationNode.get("backdrop_path").asText());
                    animation.setGenreIds(objectMapper.convertValue(animationNode.get("genre_ids"), List.class));
                    animation.setVoteAverage(animationNode.get("vote_average").asDouble());
                    animation.setVoteCount(animationNode.get("vote_count").asInt());
                    animation.setPopularity(animationNode.get("popularity").asDouble());


                    Video video = animationVideoService.getFirstVideoForAnimation(animationId);
                    if (video != null) {
                        video.setAnimation(animation);
                        animation.getVideos().add(video);
                        System.out.println("Added video: " + video.getName());
                    }


                    animationRepository.save(animation);
                    System.out.println("Saved animation: " + animation.getName());


                    animationDirectorService.saveAnimationDirectors(animationId);


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
