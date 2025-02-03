package com.example.CineHive.repository.MovieAndDrama;

import com.example.CineHive.entity.Drama;
import com.example.CineHive.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DramaRepository extends JpaRepository<Drama, Long> {
}
