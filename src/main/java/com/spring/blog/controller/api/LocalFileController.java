package com.spring.blog.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Profile("test")
@RestController
@RequiredArgsConstructor
public class LocalFileController {

    @Value("${file.dir}")
    private String dir;

    @GetMapping("/user/defaultImage")
    public ResponseEntity<Resource> defaultUserImage() {
        Resource resource = new FileSystemResource(dir + "/user/default_user.png");

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> uploadUserImage(@PathVariable("filename") String filename) {
        Resource resource = new FileSystemResource(dir + "/user/" + filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

    @PutMapping("/article/{filename}")
    public ResponseEntity<String> uploadArticleImage(@PathVariable("filename") String filename,
                                                     HttpServletRequest request) {

        Path path = Paths.get(dir, "/article");

        Path filePath = path.resolve(filename);

        try (InputStream inputStream = request.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패");
        }

        return ResponseEntity.ok().body("/article/" + filename);
    }

    @GetMapping("/article/{filename}")
    public ResponseEntity<Resource> uploadArticleImage(@PathVariable("filename") String filename) {
        Resource resource = new FileSystemResource(dir + "/article/" + filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }
}
