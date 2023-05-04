package com.example.audioservice.service;

import com.example.audioservice.domain.MusicFile;
import com.example.audioservice.repo.MusicRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Service
public class MusicService {

    private final MusicRepo musicRepo;
    private final FileService fileService;

    public MusicService(MusicRepo musicRepo, FileService fileService) {
        this.musicRepo = musicRepo;
        this.fileService = fileService;
    }
    public MusicFile upload(MultipartFile file) {
        String name = fileService.store(file);
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/upload/")
                .path(name)
                .toUriString();
        MusicFile newTrack = new MusicFile();
        newTrack.setName(file.getOriginalFilename());
        newTrack.setUri(uri);
        newTrack.setAuthor(newTrack.getAuthor());
        newTrack.setAlbum(newTrack.getAlbum());
        newTrack.setSize(file.getSize());
        return musicRepo.save(newTrack);
    }
    public List<MusicFile> getAllTracks() {
        return musicRepo.findAll();
    }
    public MusicFile getById(Long id) {
        return musicRepo.findById(id).orElseThrow();
    }
    public void delete(MusicFile file)  {
        musicRepo.delete(file);
        try {
            fileService.deleteFile(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
