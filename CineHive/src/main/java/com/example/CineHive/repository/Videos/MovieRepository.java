package com.example.CineHive.repository.Videos;

import com.example.CineHive.entity.videoType.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
