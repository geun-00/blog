package com.spring.blog.common;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("test")
@RequiredArgsConstructor
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        int userCount = 10;
        int articleCount = 100;
        int commentCount = 100;

        List<User> users = new ArrayList<>();
        for (int i = 1; i <= userCount; i++) {
            users.add(User.builder()
                    .email("test" + i + "@email.com")
                    .password(passwordEncoder.encode("rmsdud12@" + i))
                    .registrationId(SocialType.NONE)
                    .nickname("nickname" + i)
                    .build());
        }
        userRepository.saveAll(users);

        List<Article> articles = new ArrayList<>();
        for (User user : users) {
            for (int i = 1; i <= articleCount; i++) {
                articles.add(new Article(
                        i + "번째 글",
                        i + "번째 제목",
                        user));
            }
        }

        blogRepository.saveAll(articles);

        List<Comment> comments = new ArrayList<>();
        for (Article article : articles) {
            for (int i = 1; i <= commentCount; i++) {
                comments.add(Comment.builder()
                        .content("테스트 댓글입니다.")
                        .article(article)
                        .user(article.getUser())
                        .build()
                );
            }
        }

        commentRepository.saveAll(comments);
    }
}