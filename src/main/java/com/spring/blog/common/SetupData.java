package com.spring.blog.common;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        int userCount = 10;
        int articleCount = 100;

        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            users.add(User.builder()
                    .email("test" + i + "@email.com")
                    .password(passwordEncoder.encode("rmsdud12@" + i))
                    .phoneNumber("0104016124" + i)
                    .profileImageUrl("/user/defaultImage")
                    .registrationId(SocialType.NONE)
                    .nickname("nickname" + i)
                    .build());
        }
        userRepository.saveAll(users);

        List<Article> articles = new ArrayList<>();
        for (User user : users) {
            for (int i = 1; i <= articleCount; i++) {
                articles.add(Article.builder()
                        .title(i + "번째 글")
                        .content(i + "번째 제목")
                        .user(user)
                        .build());
            }
        }
        saveDummyArticles(articles);
    }

    private void saveDummyArticles(List<Article> articles) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO BLOG.PUBLIC.ARTICLES (LIKES, CREATED_AT, UPDATED_AT, USER_ID, VIEWS, TITLE, CONTENT) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Article article = articles.get(i);

                        ps.setInt(1, 0);
                        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusHours(i)));
                        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusHours(i)));
                        ps.setLong(4, article.getUser().getId());
                        ps.setLong(5, 0);
                        ps.setString(6, article.getTitle());
                        ps.setString(7, article.getContent());
                    }

                    @Override
                    public int getBatchSize() {
                        return articles.size();
                    }
                }
        );
    }
}