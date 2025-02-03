package com.example.CineHive.repository.Videos;

import com.example.CineHive.entity.Video.TopMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopMovieRepository extends JpaRepository<TopMovie, Long>{
}
