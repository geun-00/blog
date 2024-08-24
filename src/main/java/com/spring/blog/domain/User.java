package com.spring.blog.domain;

import com.spring.blog.common.converters.enums.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name="registration_id")
    @Enumerated(EnumType.STRING)
    private SocialType registrationId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    public void addArticle(Article article) {
        articles.add(article);
        article.setUser(this);
    }

    @Builder
    public User(String email, String password, String nickname, SocialType registrationId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.registrationId = registrationId;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}