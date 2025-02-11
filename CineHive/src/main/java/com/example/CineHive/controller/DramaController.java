package com.example.CineHive.controller;

import com.example.CineHive.entity.videotype.Drama;
import com.example.CineHive.repository.videos.drama.DramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class DramaController {

    @Autowired
    private DramaRepository dramaRepository;

    @GetMapping("/dramas/{id}")
    @ResponseBody
    public ResponseEntity<Drama> getDramaById(@PathVariable Long id) {
        Optional<Drama> dramaOptional = dramaRepository.findById(id);
        if (dramaOptional.isPresent()) {
            return ResponseEntity.ok(dramaOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dramas")
    @ResponseBody
    public List<Drama> getDramas(){
        return dramaRepository.findAll();
    }
}
