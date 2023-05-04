package com.example.audioservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "musicFiles")
public class MusicFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String album;
    private String author;
    private Long size;
    private String uri;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicFile)) return false;
        MusicFile musicFile = (MusicFile) o;
        return Objects.equals(id, musicFile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
