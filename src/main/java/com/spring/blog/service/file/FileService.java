package com.spring.blog.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * @param file 저장할 파일
     * @param dir 파일이 저장될 디렉터리
     * @return 저장된 파일의 경로
     */
    String saveFile(MultipartFile file, String dir);
}