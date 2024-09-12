package com.spring.blog.controller.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class LocalFileController {

    @Value("${file.dir}")
    private String dir;

    @GetMapping("/user/defaultImage")
    public ResponseEntity<Resource> defaultUserImage() {
        Resource resource = new FileSystemResource(dir + "/user/default_user.png");

        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> uploadUserImage(@PathVariable("filename") String filename) {
        Resource resource = new FileSystemResource(dir + "/user/" + filename);

        return ResponseEntity.ok().body(resource);
    }
}
