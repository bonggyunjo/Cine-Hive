package com.example.CineHive.repository.videos.movie;


import com.example.CineHive.entity.videotype.TopMovie;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopMovieRepository extends JpaRepository<TopMovie, Long> {

}
