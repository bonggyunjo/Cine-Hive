package com.example.CineHive.repository.Videos;

import com.example.CineHive.entity.Video.Animation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimationRepository extends JpaRepository<Animation, Long> {
}
