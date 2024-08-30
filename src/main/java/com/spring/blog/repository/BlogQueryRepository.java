package com.spring.blog.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.blog.dto.response.ArticleListViewResponse;
import com.spring.blog.dto.request.ArticleSearchRequest;
import com.spring.blog.dto.response.QArticleListViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.spring.blog.domain.QArticle.article;
import static com.spring.blog.domain.QComment.comment;

@Repository
@RequiredArgsConstructor
public class BlogQueryRepository {

    private final JPAQueryFactory query;

    /**
     * 조회 조건 X
     **/
    public Page<ArticleListViewResponse> findAll(Pageable pageable) {
        return findArticlesByCond(null, pageable);
    }

    /**
     * 조회 조건 O
     **/
    public Page<ArticleListViewResponse> findAllByCond(ArticleSearchRequest request, Pageable pageable) {
        return findArticlesByCond(request, pageable);
    }

    private Page<ArticleListViewResponse> findArticlesByCond(ArticleSearchRequest request, Pageable pageable) {

        List<ArticleListViewResponse> content = query
                .select(new QArticleListViewResponse(
                        article,
                        JPAExpressions  //서브 쿼리
                                .select(comment.count())
                                .from(comment)
                                .where(comment.article.eq(article))
                ))
                .from(article)
                .where(getCondByRequest(request))
                .join(article.user).fetchJoin()
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

        if (request == null) {
            return null;
        }

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