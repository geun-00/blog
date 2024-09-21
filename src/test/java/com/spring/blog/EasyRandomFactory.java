package com.spring.blog;

import com.spring.blog.common.enums.SearchType;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.response.AddArticleViewResponse;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import com.spring.blog.service.dto.response.PageResponse;
import com.spring.blog.service.dto.response.UserInfoResponse;
import net.datafaker.Faker;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.jeasy.random.FieldPredicates.inClass;
import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class EasyRandomFactory {

    private static final Faker FAKER = new Faker();

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

    public static UserInfoResponse createUserInfoResponse() {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .objectFactory(new RecordFactory());

        EasyRandom easyRandom = new EasyRandom(parameters);

        return easyRandom.nextObject(UserInfoResponse.class);
    }

    public static PrincipalUser createPrincipalUser() {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(ProviderUser.class, FakeProviderUser::new)
                .objectFactory(new RecordFactory());

        EasyRandom easyRandom = new EasyRandom(parameters);

        PrincipalUser principalUser = easyRandom.nextObject(PrincipalUser.class);
        return principalUser.withUpdatedUser(null);
    }

    public static ArticleSearchServiceRequest createArticleSearchServiceRequest(SearchType searchType) {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(SearchType.class, () -> searchType)
                .objectFactory(new RecordFactory());

        EasyRandom easyRandom = new EasyRandom(parameters);

        return easyRandom.nextObject(ArticleSearchServiceRequest.class);
    }

    public static ArticleSearchRequest createArticleSearchRequest(SearchType searchType) {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(SearchType.class, () -> searchType)
                .objectFactory(new RecordFactory());

        EasyRandom easyRandom = new EasyRandom(parameters);

        return easyRandom.nextObject(ArticleSearchRequest.class);
    }

    private static class FakeProviderUser implements ProviderUser {

        private final String id;
        private final String username;
        private final String password;
        private final String email;
        private final String profileImageUrl;
        private final List<? extends GrantedAuthority> authorities;

        public FakeProviderUser() {
            this.id = FAKER.idNumber().valid();
            this.username = "nickname0";
            this.password = "password12@";
            this.email = "user@test.com";
            this.profileImageUrl = FAKER.image().base64GIF();
            this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        @Override
        public List<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }
    }
}
