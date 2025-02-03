package com.example.CineHive.service;

import com.example.CineHive.dto.MovieVideoDto;
import com.example.CineHive.entity.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieVideoService {
    private final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    @Value("${tmdb.api.key}") // properties 파일에서 API 키를 주입
    private String apiKey;

    private final RestTemplate restTemplate;

    public MovieVideoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Video> getVideosForMovie(Long movieId) {
        String url = BASE_URL + movieId + "/videos?api_key=" + apiKey; // 수정된 부분
        MovieVideoDto videoResponse = restTemplate.getForObject(url, MovieVideoDto.class);

        if (videoResponse != null && videoResponse.getResults() != null) {
            return videoResponse.getResults().stream()
                    .map(videoResult -> {
                        Video video = new Video();
                        video.setVideoKey(videoResult.getKey());
                        video.setName(videoResult.getName());
                        video.setSite(videoResult.getSite());
                        video.setType(videoResult.getType());
                        return video;
                    })
                    .collect(Collectors.toList());
        }
        return List.of(); // 비디오가 없으면 빈 리스트 반환
    }
}
