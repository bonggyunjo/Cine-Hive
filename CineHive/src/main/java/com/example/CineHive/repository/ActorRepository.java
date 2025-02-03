package com.example.CineHive.repository;

import com.example.CineHive.entity.credit.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
}
