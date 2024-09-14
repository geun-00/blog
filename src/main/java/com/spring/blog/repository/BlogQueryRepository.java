package com.spring.blog.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.blog.domain.User;
import com.spring.blog.mapper.ArticleMapper;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.response.ArticleInfo;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.QArticleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.spring.blog.domain.QArticle.article;
import static com.spring.blog.domain.QArticleLikes.articleLikes;
import static com.spring.blog.domain.QComment.comment;

@Repository
@RequiredArgsConstructor
public class BlogQueryRepository {

    private final JPAQueryFactory query;
    private final ArticleMapper articleMapper;

    /**
     * 사용자 제거할 때 좋아요 눌렀던 게시글들 좋아요 수 1 감소시키기
     */
    public void decreaseArticleLikesByUser(User user) {
        query
                .update(article)
                .set(article.likes, article.likes.subtract(1))
                .where(article.id.in(
                        JPAExpressions
                                .select(articleLikes.article.id)
                                .from(articleLikes)
                                .where(articleLikes.user.eq(user))
                ))
                .execute();
    }

    /**
     * 조회 조건 X
     **/
    public Page<ArticleListViewResponse> findAll(Pageable pageable) {
        return findArticlesByCond(null, pageable);
    }

    /**
     * 조회 조건 O
     **/
    public Page<ArticleListViewResponse> findAllByCond(ArticleSearchServiceRequest request, Pageable pageable) {
        return findArticlesByCond(request, pageable);
    }

    private Page<ArticleListViewResponse> findArticlesByCond(ArticleSearchServiceRequest request, Pageable pageable) {

        List<ArticleInfo> articleInfos = query
                .select(new QArticleInfo(
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

        List<ArticleListViewResponse> content = articleInfos
                .stream()
                .map(articleMapper::toArticleListViewResponse)
                .toList();

        JPAQuery<Long> countQuery = query
                .select(article.count())
                .from(article)
                .where(getCondByRequest(request));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression getCondByRequest(ArticleSearchServiceRequest request) {

        if (request == null) {
            return null;
        }

        switch (request.searchType()) {
            case TITLE -> { //제목으로 조회
                return containsTitle(request.title());
            }
            case CONTENT -> { //내용으로 조회
                return containsContent(request.content());
            }
            case AUTHOR -> { //작성자로 조회
                return containsAuthor(request.author());
            }
            case TITLE_CONTENT -> { //제목+내용으로 조회
                return containsTitle(request.titleContent().title())
                        .and
                      (containsContent(request.titleContent().content()));
            }
            case PERIOD -> { //기간으로 조회
                return betweenPeriod(request.period().startDate(),
                                     request.period().endDate());
            }
        }

        throw new IllegalArgumentException("검색 조건 예외 발생");
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
}