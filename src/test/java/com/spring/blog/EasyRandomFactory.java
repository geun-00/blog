package com.spring.blog;

import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.service.dto.response.AddArticleViewResponse;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import com.spring.blog.service.dto.response.PageResponse;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.jeasy.random.FieldPredicates.inClass;
import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class EasyRandomFactory {

    public static PageResponse<ArticleListViewResponse> createPageResponse(int size, int currentPage, int pageSize) {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .objectPoolSize(size)
                .collectionSizeRange(1, 10)
                .stringLengthRange(5, 20)
                .randomize(Long.class, () -> new Random().nextLong(1, size + 1))
                .objectFactory(new RecordFactory());

        EasyRandom easyRandom = new EasyRandom(parameters);

        List<ArticleListViewResponse> articles = IntStream.range(0, size)
                .mapToObj(i -> easyRandom.nextObject(ArticleListViewResponse.class))
                .toList();

        long totalCount = articles.size();

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, articles.size());

        return PageResponse.<ArticleListViewResponse>withAll()
                .dataList(articles.subList(start, end))
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .build();
    }

    public static List<Article> createArticles(int size, User user) {

        Predicate<Field> id = named("id").and(ofType(Long.class)).and(inClass(Article.class));

        EasyRandomParameters parameters = new EasyRandomParameters()
                .excludeField(id)
                .objectPoolSize(size)
                .stringLengthRange(5, 20)
                .randomize(Long.class, () -> new Random().nextLong(1, size + 1))
                .randomize(User.class, () -> user);

        EasyRandom easyRandom = new EasyRandom(parameters);

        return IntStream.range(0, size)
                .mapToObj(o -> easyRandom.nextObject(Article.class))
                .toList();
    }

    public static ArticleViewResponse createArticleViewResponse(Long articleId) {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(Long.class, () -> new Random().nextLong(1, 101))
                .objectFactory(new RecordFactory());

        EasyRandom easyRandom = new EasyRandom(parameters);

        return easyRandom.nextObject(ArticleViewResponse.class);
    }

    public static AddArticleViewResponse createAddArticleViewResponse() {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .objectFactory(new RecordFactory());

        EasyRandom easyRandom = new EasyRandom(parameters);

        return easyRandom.nextObject(AddArticleViewResponse.class);
    }
}
