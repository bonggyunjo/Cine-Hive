package com.example.CineHive.service.creditService.drama;

import com.example.CineHive.entity.credit.drama.Director;
import com.example.CineHive.entity.video.Drama;
import com.example.CineHive.repository.videos.drama.DramaDirectorRepository;
import com.example.CineHive.repository.videos.drama.DramaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DramaDirectorService {
    @Value("${tmdb.api.key}")
    private String apiKey;

    private final WebClient webClient;

    @Autowired
    private DramaRepository dramaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DramaDirectorRepository dramaDirectorRepository;

    public DramaDirectorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    @Transactional
    public void saveDramaDirectors(Long dramaId) {
        String response = webClient.get()
                .uri("/tv/" + dramaId + "/credits?api_key=" + apiKey + "&language=en-US")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode crewNode = rootNode.path("crew");

                Drama drama = dramaRepository.findById(dramaId).orElse(null);
                if (drama != null) {
                    for (JsonNode crewMember : crewNode) {
                        // 감독 정보를 찾기 위해 "job" 속성이 "Director"인 경우만 필터링
                        if ("Director".equals(crewMember.get("job").asText())) {
                            Director director = new Director();
                            director.setName(crewMember.get("name").asText());

                            // Drama에 Director 추가
                            drama.getDirectors().add(director);
                            break; // 감독은 한 명만 있으므로 루프 탈출
                        }
                    }
                    dramaRepository.save(drama);
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
