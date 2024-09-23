package com.spring.blog.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
@Profile({"local", "test"})
@RequiredArgsConstructor
public class LocalFileService implements FileService {

    @Value("${file.dir}")
    private String uploadDir;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String saveFile(MultipartFile file, String dir) {

        Path path = Paths.get(uploadDir, dir);

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path filePath = path.resolve(filename);

        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "/" + filename;
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, "/user/" + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public String generatedPreSignedUrl(String filename) {
        return "/article/" + UUID.randomUUID() + "-" + filename + "?";
    }

    @Override
    public void cleanupFiles(String sessionId) {

        SetOperations<String, Object> so = redisTemplate.opsForSet();
        Set<Object> urls = so.members(sessionId);

        if (urls != null) {
            for (Object url : urls) {
                deleteFile((String) url);
            }
            redisTemplate.delete(sessionId);
        }
    }
}