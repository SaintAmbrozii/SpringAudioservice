package com.example.audioservice.controller;

import com.example.audioservice.service.FileService;
import com.example.audioservice.domain.MusicFile;
import com.example.audioservice.service.MusicService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tracks")
public class AudioController {

    private final FileService fileService;
    private final MusicService musicService;

    public AudioController(FileService fileService, MusicService musicService) {
        this.fileService = fileService;
        this.musicService = musicService;
    }

    @PostMapping(value = "upload",consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public MusicFile upload(@RequestPart MultipartFile file) {
        return musicService.upload(file);
    }

    @PostMapping("uploads")
    public List<MusicFile> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> upload(file))
                .collect(Collectors.toList());
    }
    @GetMapping
    public List<MusicFile> getAllTracks() {
        return musicService.getAllTracks();
    }
    @GetMapping("{id}")
    public MusicFile getById(@PathVariable(name = "id") Long id){
        return musicService.getById(id);
    }


    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.loadAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @DeleteMapping("/{id}")
    public void deleteTrack(@PathVariable(name = "id") MusicFile file) {
            musicService.delete(file);

    }
}
