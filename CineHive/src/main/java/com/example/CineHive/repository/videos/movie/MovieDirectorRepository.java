package com.example.CineHive.repository.videos.movie;

import com.example.CineHive.entity.credit.movie.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDirectorRepository extends JpaRepository<Director, Long> {
}