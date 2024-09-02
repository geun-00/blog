package com.spring.blog.service.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Profile("local")
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private static final List<String> ALLOW_FILE_TYPES = List.of(
            "image/png",
            "image/jpeg",
            "image/gif",
            "image/webp"
    );

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${profile.default.image-url}")
    private String defaultUrl;

    @Override
    public String saveFile(MultipartFile file, String dir) {
        if (file == null || !StringUtils.hasText(file.getOriginalFilename())) {
            return defaultUrl;
        }
        if (!ALLOW_FILE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다." + file.getContentType());
        }
        String originalFilename = file.getOriginalFilename();
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
            e.printStackTrace();
            throw new RuntimeException("파일 업로드 실패", e);
        }

        return s3Client.getUrl(bucket, fileName).toString();
    }
}