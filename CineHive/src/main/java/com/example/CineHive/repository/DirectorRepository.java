package com.example.CineHive.repository;

import com.example.CineHive.entity.credit.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}