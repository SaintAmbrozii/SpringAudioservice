package com.example.audioservice.repo;

import com.example.audioservice.domain.MusicFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepo extends JpaRepository<MusicFile,Long> {
}
