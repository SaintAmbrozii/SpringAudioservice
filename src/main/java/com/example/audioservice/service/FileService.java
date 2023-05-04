package com.example.audioservice.service;

import com.example.audioservice.domain.MusicFile;
import com.example.audioservice.exception.FileNotFoundException;
import com.example.audioservice.exception.FileServiceException;
import com.example.audioservice.repo.MusicRepo;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final Path rootLocation;
    private final MusicRepo musicRepo;


    public FileService(Environment env, MusicRepo musicRepo) {
        this.rootLocation = Paths.get(env.getProperty("app.file.upload-dir", "./upload"))
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }


        this.musicRepo = musicRepo;
    }
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(String.valueOf(file.getOriginalFilename().lastIndexOf(".") + 1));
        try {
            if (file.isEmpty()) {
                throw new FileServiceException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new FileServiceException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new FileServiceException("Failed to store file " + filename, e);
        }

        return filename;
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }
    public void deleteFile(String filename) throws IOException {
        Path filepath = this.rootLocation.resolve(filename);
        FileSystemUtils.deleteRecursively(filepath);

    }

}
