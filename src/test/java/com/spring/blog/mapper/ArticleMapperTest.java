package com.spring.blog.mapper;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.common.enums.SearchType;
import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.Comment;
import com.spring.blog.domain.User;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.request.ArticleServiceRequest;
import com.spring.blog.service.dto.response.AddArticleViewResponse;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import com.spring.blog.service.dto.response.CommentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleMapperTest {

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentResponseMapper commentResponseMapper;

    private final ArticleMapper articleMapper = new ArticleMapperImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(articleMapper, "commentResponseMapper", commentResponseMapper);
    }

    @DisplayName("ArticleServiceRequest 매핑 테스트")
    @Test
    void articleServiceRequest() {
        // given
        String title = "test title";
        String content = "test content";
        ArticleRequest request = new ArticleRequest(title, content);

        // when
        ArticleServiceRequest serviceRequest = articleMapper.toServiceRequest(request);

        // then
        assertThat(serviceRequest.title()).isEqualTo(title);
        assertThat(serviceRequest.content()).isEqualTo(content);
    }

    @DisplayName("ArticleSearchServiceRequest 매핑 테스트")
    @Test
    void articleSearchServiceRequest() {
        // given
        SearchType searchType = SearchType.AUTHOR;
        String title = "hAcncujX";
        String content = "eefRSuVb";
        String author = "OlgaShi";
        ArticleSearchRequest.TitleContent titleContent = new ArticleSearchRequest.TitleContent(
                title, content
        );
        ArticleSearchRequest.Period period = new ArticleSearchRequest.Period(
                LocalDate.now(), LocalDate.now()
        );

        ArticleSearchRequest request = new ArticleSearchRequest(
                searchType, title, content, author, titleContent, period);

        // when
        ArticleSearchServiceRequest serviceRequest = articleMapper.toServiceRequest(request);

        // then
        assertThat(serviceRequest.title()).isEqualTo(title);
        assertThat(serviceRequest.content()).isEqualTo(content);
        assertThat(serviceRequest.author()).isEqualTo(author);
        assertThat(serviceRequest.titleContent()).extracting("title", "content").containsExactly(
                titleContent.title(),
                titleContent.content()
        );
        assertThat(serviceRequest.period()).extracting("startDate", "endDate").containsExactly(
                period.startDate(),
                period.endDate()
        );
    }

    @DisplayName("ArticleViewResponse 매핑 테스트")
    @Test
    void articleViewResponse() {
        // given
        User user = EasyRandomFactory.createUser();
        List<Article> articles = EasyRandomFactory.createArticles(1, user);

        Article article = articles.get(0);

        Comment comment = Comment.builder()
                .user(user)
                .article(article)
                .content("YVAww1dr")
                .build();

        CommentResponse target = new CommentResponse(
                1L, user.getNickname(), comment.getContent(), user.getProfileImageUrl(), LocalDateTime.now(), true
        );
        when(commentMapper.toResponse(any(Comment.class), anyBoolean())).thenReturn(target);

        // when
        ArticleViewResponse response = articleMapper.toArticleViewResponse(article);

        // then
        verify(commentMapper, times(1)).toResponse(any(Comment.class), anyBoolean());

        assertThat(response.id()).isEqualTo(article.getId());
        assertThat(response.views()).isEqualTo(article.getViews());
        assertThat(response.likes()).isEqualTo(article.getLikes());
        assertThat(response.content()).isEqualTo(article.getContent());
        assertThat(response.profileImageUrl()).isEqualTo(article.getUser().getProfileImageUrl());
        assertThat(response.author()).isEqualTo(article.getUser().getNickname());
        assertThat(response.createdAt()).isEqualTo(article.getCreatedAt());

        CommentResponse commentResponse = response.comments().get(0);
        assertThat(commentResponse.id()).isEqualTo(target.id());
        assertThat(commentResponse.username()).isEqualTo(target.username());
        assertThat(commentResponse.comment()).isEqualTo(target.comment());
        assertThat(commentResponse.isAuthor()).isEqualTo(target.isAuthor());
        assertThat(commentResponse.createdAt()).isEqualTo(target.createdAt());
        assertThat(commentResponse.profileImageUrl()).isEqualTo(target.profileImageUrl());
    }

    @DisplayName("AddArticleViewResponse 매핑 테스트")
    @Test
    void addArticleViewResponse() {
        // given
        Article article = Article.builder()
                .title("dOplgHNCkO")
                .content("MichelMia")
                .build();

        ReflectionTestUtils.setField(article, "id", 1L);

        // when
        AddArticleViewResponse response = articleMapper.toAddArticleViewResponse(article);

        // then
        assertThat(response.id()).isEqualTo(article.getId());
        assertThat(response.title()).isEqualTo(article.getTitle());
        assertThat(response.content()).isEqualTo(article.getContent());
    }
}