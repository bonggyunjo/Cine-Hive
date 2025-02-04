package com.example.CineHive.service.creditService.animation;

import com.example.CineHive.entity.credit.animation.Director; // 애니메이션 감독 엔티티
import com.example.CineHive.entity.video.Animation; // 애니메이션 엔티티
import com.example.CineHive.repository.videos.animation.AnimationRepository; // 애니메이션 리포지토리
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AnimationDirectorService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;

    @Autowired
    private AnimationRepository animationRepository;

    @Autowired
    private ObjectMapper objectMapper;


    public AnimationDirectorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    @Transactional
    public void saveAnimationDirectors(Long animationId) {
        String response = webClient.get()
                .uri("/movie/" + animationId + "/credits?api_key=" + apiKey + "&language=en-US")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode crewNode = rootNode.path("crew");

                Animation animation = animationRepository.findById(animationId).orElse(null);
                if (animation != null) {
                    for (JsonNode crewMember : crewNode) {
                        // 감독 정보를 찾기 위해 "job" 속성이 "Director"인 경우만 필터링
                        if ("Director".equals(crewMember.get("job").asText())) {
                            Director director = new Director();
                            director.setName(crewMember.get("name").asText());
                            director.setAnimation(animation);
                            // Animation에 Director 추가
                            animation.getDirectors().add(director);
                            break; // 감독은 한 명만 있으므로 루프 탈출
                        }
                    }
                    animationRepository.save(animation);
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
