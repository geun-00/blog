package com.spring.blog.common;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.dto.request.AddArticleRequest;
import com.spring.blog.dto.request.CommentRequest;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.BlogService;
import com.spring.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("test")
@RequiredArgsConstructor
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {

    private final BlogService blogService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentService commentService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        for (int i = 1; i <= 10; i++) {

            User savedUser = userRepository.save(User.builder()
                    .email("test" + i + "@email.com")
                    .password(passwordEncoder.encode("rmsdud12@" + i))
                    .registrationId(SocialType.NONE)
                    .nickname("nickname" + i)
                    .build());

            for (int j = 1; j <= 10; j++) {

                AddArticleRequest request = new AddArticleRequest();
                request.setTitle(j + "번째 글 제목");
                request.setContent(j + "번째 글 내용");
                Article savedArticle = blogService.save(request, savedUser.getEmail());

                for (int k = 1; k <= 10; k++) {

                    CommentRequest commentRequest = new CommentRequest();
                    commentRequest.setArticleId(savedArticle.getId());
                    commentRequest.setComment(k + "번째 댓글");

                    commentService.addComment(commentRequest, savedUser.getEmail());
                }
            }
        }
    }
}