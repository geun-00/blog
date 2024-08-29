package com.spring.blog.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.blog.domain.Article;
import com.spring.blog.dto.ArticleSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.spring.blog.domain.QArticle.article;

@Repository
@RequiredArgsConstructor
public class BlogQueryRepository {

    private final JPAQueryFactory query;


    public Page<Article> findAllByCond(ArticleSearchRequest request, Pageable pageable) {

        List<Article> content = query.selectFrom(article)
                .join(article.user).fetchJoin()
                .where(getCondByRequest(request))
                .orderBy(article.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(article.count())
                .from(article)
                .where(getCondByRequest(request));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression getCondByRequest(ArticleSearchRequest request) {

        switch (request.getSearchType()) {
            case TITLE -> { //제목으로 조회
                return containsTitle(request.getTitle());
            }
            case CONTENT -> { //내용으로 조회
                return containsContent(request.getContent());
            }
            case AUTHOR -> { //작성자로 조회
                return containsAuthor(request.getAuthor());
            }
            case TITLE_CONTENT -> { //제목+내용으로 조회
                return containsTitle(request.getTitleContent().getTitle())
                        .and
                      (containsContent(request.getTitleContent().getContent()));
            }
            case PERIOD -> { //기간으로 조회
                return betweenPeriod(request.getPeriod().getStartDate(),
                                     request.getPeriod().getEndDate());
            }
            case VIEWS -> { //조회수로 조회
                return goeViews(request.getViews());
            }
        }

        throw new IllegalArgumentException("예외 발생");
    }

    private BooleanExpression containsTitle(String title) {
        return article.title.contains(title);
    }

    private BooleanExpression containsContent(String content) {
        return article.content.contains(content);
    }

    private BooleanExpression containsAuthor(String author) {
        return article.user.nickname.contains(author);
    }

    private BooleanExpression betweenPeriod(LocalDate startDate, LocalDate endDate) {
        return article.createdAt.between(startDate.atStartOfDay(),
                endDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59));
    }

    private BooleanExpression goeViews(Long views) {
        return article.views.goe(views);
    }
}