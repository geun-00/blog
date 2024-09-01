package com.spring.blog.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.blog.dto.response.QUserInfoResponse;
import com.spring.blog.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.spring.blog.domain.QArticle.article;
import static com.spring.blog.domain.QComment.comment;
import static com.spring.blog.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory query;

    public UserInfoResponse getUserInfo(String name) {

        return query
                .select(new QUserInfoResponse(
                        user,
                        JPAExpressions
                                .select(article.count())
                                .from(article)
                                .where(article.user.eq(user)),
                        JPAExpressions
                                .select(comment.count())
                                .from(comment)
                                .where(comment.user.eq(user))
                ))
                .from(user)
                .where(user.nickname.eq(name))
                .fetchOne();
    }
}