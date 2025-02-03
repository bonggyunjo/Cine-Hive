package com.example.CineHive.repository.Videos;

import com.example.CineHive.entity.VideoType.TopMovie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopMovieRepository extends JpaRepository<TopMovie, Long>{

    @Query("SELECT m FROM TopMovie m ORDER BY m.popularity DESC")
    List<TopMovie> findTopMovies(Pageable pageable);
}
