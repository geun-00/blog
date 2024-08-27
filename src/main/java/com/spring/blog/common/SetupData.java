package com.spring.blog.common;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.domain.User;
import com.spring.blog.dto.AddArticleRequest;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.BlogService;
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

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        User savedUser = userRepository.save(User.builder()
                .email("test@email.com")
                .password(passwordEncoder.encode("rmsdud12@"))
                .registrationId(SocialType.NONE)
                .nickname("nickname")
                .build());

        for (int i = 1; i <= 1111; i++) {

            AddArticleRequest request = new AddArticleRequest();
            request.setTitle(i + "번째 글 제목");
            request.setContent(i + "번째 글 내용");

            blogService.save(request, savedUser.getEmail());
        }
    }
}