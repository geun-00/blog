package com.spring.blog.service.file;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@Profile("local")
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final AmazonS3Client s3Client;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${profile.default.image-url}")
    private String defaultUrl;

    @Override
    public String saveFile(MultipartFile file, String dir) {

        if (file == null || !StringUtils.hasText(file.getOriginalFilename())) {
            return defaultUrl;
        }
        validImageFile(file);

        String originalFilename = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);
        String fileName = UUID.randomUUID() + "-" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            s3Client.putObject(new PutObjectRequest(
                    bucket,
                    dir + fileName,
                    file.getInputStream(),
                    metadata
            ));
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("파일 업로드 실패", e);
        }

        return s3Client.getUrl(bucket, dir + fileName).toString();
    }

    @Override
    public void deleteFile(String fileName) {
        log.info("deleteFile 비동기 처리 : {}", Thread.currentThread());
        String decode = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        s3Client.deleteObject(bucket, decode.substring(49));
    }

    @Override
    public String generatedPreSignedUrl(String filename) {
        Date expiration = new Date(new Date().getTime() + (1000 * 60 * 3)); //3분

        String key = "articles/" + UUID.randomUUID() + "-" + filename;

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    @Override
    public void cleanupFiles(String sessionId) {

        log.info("cleanupFiles 비동기 처리 : {}", Thread.currentThread());

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