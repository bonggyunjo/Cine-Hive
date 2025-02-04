package com.example.CineHive.controller;

import com.example.CineHive.entity.video.Animation;
import com.example.CineHive.repository.videos.animation.AnimationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class AnimationController {

    @Autowired
    private AnimationRepository animationRepository;

    @GetMapping("/animations/{id}")
    @ResponseBody
    public ResponseEntity<Animation> getAnimationById(@PathVariable Long id) {
        Optional<Animation> animationOptional = animationRepository.findById(id);
        if (animationOptional.isPresent()) {
            return ResponseEntity.ok(animationOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
