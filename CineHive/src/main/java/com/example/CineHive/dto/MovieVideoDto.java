package com.example.CineHive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieVideoDto {
    private List<VideoResult> results;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoResult {
        private String key;
        private String name;
        private String site;
        private String type;

        // Getters and Setters
    }
}
