package com.example.CineHive.repository.Videos;

import com.example.CineHive.entity.VideoType.Drama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DramaRepository extends JpaRepository<Drama, Long> {
}
