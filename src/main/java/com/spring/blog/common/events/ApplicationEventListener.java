package com.spring.blog.common.events;

import com.spring.blog.domain.ArticleImages;
import com.spring.blog.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    private final FileService fileService;

    @Async
    @TransactionalEventListener
    public void articleDeletedEvent(ArticleDeletedEvent event) {
        log.info("게시글 삭제 트랜잭션 커밋 후 이벤트 수행");

        for (ArticleImages articleImage : event.getArticleImages()) {
            try {
                fileService.deleteFile(articleImage.getImageUrl());
                log.info("파일 삭제 성공: {}", articleImage.getImageUrl());
            } catch (Exception e) {
                log.error("파일 삭제 실패: {}", articleImage.getImageUrl(), e);
            }
        }
    }

    @Async
    @TransactionalEventListener
    public void userDeletedEvent(UserDeletedEvent event) {
        log.info("사용자 삭제 트랜잭션 커밋 후 이벤트 수행");

        try {
            fileService.deleteFile(event.getProfileImageUrl());
            log.info("파일 삭제 성공: {}", event.getProfileImageUrl());
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", event.getProfileImageUrl(), e);
        }

        for (ArticleImages articleImage : event.getArticleImages()) {
            try {
                fileService.deleteFile(articleImage.getImageUrl());
                log.info("파일 삭제 성공: {}", articleImage.getImageUrl());
            } catch (Exception e) {
                log.error("파일 삭제 실패: {}", articleImage.getImageUrl(), e);
            }
        }
    }
}