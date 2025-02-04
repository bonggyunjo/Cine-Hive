package com.example.CineHive.repository;

import com.example.CineHive.entity.credit.movie.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}