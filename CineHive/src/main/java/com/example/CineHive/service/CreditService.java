package com.example.CineHive.service;

import com.example.CineHive.entity.Actor;
import com.example.CineHive.entity.Movie;
import com.example.CineHive.repository.ActorRepository;
import com.example.CineHive.repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditService {
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    public void saveCreditsToDatabase(Long movieId, JsonNode credits) {
        Movie movie = movieRepository.findById(movieId).orElse(null);

        if (movie != null) {
            // 출연진 처리
            for (JsonNode actorNode : credits.path("cast")) {
                Actor actor = new Actor();
                actor.setName(actorNode.get("name").asText());
                actor.setOriginalName(actorNode.get("original_name").asText());
                actor.setCharacter(actorNode.get("character").asText());
                actor.setGender(actorNode.get("gender").asInt());
                actor.setMovie(movie); // 영화와 연결

                // 저장 로직
                actorRepository.save(actor);
            }

        }
    }

}
