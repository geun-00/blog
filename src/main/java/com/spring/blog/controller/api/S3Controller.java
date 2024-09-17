package com.spring.blog.controller.api;

import com.spring.blog.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final FileService fileService;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/api/s3-upload-url")
    public ApiResponse<String> getPreSignedUrl(@RequestPart("file") MultipartFile file,
                                               @CookieValue("JSESSIONID") String sessionID) {
        fileService.validImageFile(file);

        String url = fileService.generatedPreSignedUrl(file.getOriginalFilename());

        SetOperations<String, Object> so = redisTemplate.opsForSet();
        Set<Object> members = so.members(sessionID);

        if (members == null) {
            members = Collections.emptySet();
        }

        String baseUrl = url.substring(0, url.lastIndexOf("?"));

        if (!members.contains(baseUrl)) {
            so.add(sessionID, baseUrl);
            log.info("레디스에 임시 파일 저장 : {}", baseUrl);
        }

        return ApiResponse.ok(url);
    }
}