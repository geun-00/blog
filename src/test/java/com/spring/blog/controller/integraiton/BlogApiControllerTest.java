package com.spring.blog.controller.integraiton;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.annotation.WithPrincipalUser;
import com.spring.blog.common.enums.SearchType;
import com.spring.blog.controller.ApiControllerIntegrationTestSupport;
import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.User;
import com.spring.blog.parameterized.PageInfoData;
import com.spring.blog.repository.BulkInsertRepository;
import com.spring.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.spring.blog.common.enums.SearchType.AUTHOR;
import static com.spring.blog.common.enums.SearchType.CONTENT;
import static com.spring.blog.common.enums.SearchType.PERIOD;
import static com.spring.blog.common.enums.SearchType.TITLE;
import static com.spring.blog.common.enums.SearchType.TITLE_CONTENT;
import static com.spring.blog.common.enums.SearchType.values;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("블로그 API 통합 테스트")
@Transactional
public class BlogApiControllerTest extends ApiControllerIntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BulkInsertRepository bulkInsertRepository;

    private static Stream<Arguments> createSearchRequests() {
        return Stream.of(values())
                .map(searchType -> Arguments.of(searchType, new ArticleSearchRequest(
                        searchType,
                        searchType == TITLE ? "0" : null,
                        searchType == CONTENT ? "1" : null,
                        searchType == AUTHOR ? "1" : null,
                        searchType == TITLE_CONTENT ? new ArticleSearchRequest.TitleContent("0", "0") : null,
                        searchType == PERIOD ? new ArticleSearchRequest.Period(
                                LocalDate.now().minusDays(50), LocalDate.now().minusDays(1)) : null
                )));
    }

    @DisplayName("POST /api/articles 테스트")
    @TestFactory
    @WithPrincipalUser
    Collection<DynamicTest> addArticle() {

        MockHttpSession session = new MockHttpSession();
        Cookie cookie = new Cookie("JSESSIONID", session.getId());

        return List.of(
                DynamicTest.dynamicTest("정상 흐름", () -> {

                    ArticleRequest articleRequest = new ArticleRequest("title", "content");

                    mockMvc.perform(
                                    post("/api/articles")
                                            .cookie(cookie)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(articleRequest)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(201))
                            .andExpect(jsonPath("$.status").value("CREATED"))
                            .andExpect(jsonPath("$.message").value("CREATED"))
                            .andExpect(jsonPath("$.data.title").value("title"))
                            .andExpect(jsonPath("$.data.content").value("content"))
                            .andDo(print());
                }),

                DynamicTest.dynamicTest("예외 상황, MethodArgumentNotValidException 발생 검증", () -> {

                    ArticleRequest articleRequest = new ArticleRequest("", "");

                    MvcResult result = mockMvc.perform(
                                    post("/api/articles")
                                            .cookie(cookie)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(articleRequest)))
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.code").value(400))
                            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                            .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                            .andExpect(jsonPath("$.data").isEmpty())
                            .andDo(print())
                            .andReturn();

                    Exception exception = result.getResolvedException();
                    assertThat(exception).isNotNull();
                    assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
                })
        );
    }

    @DisplayName("PUT /api/articles/{articleId} 테스트")
    @TestFactory
    @WithPrincipalUser
    Collection<DynamicTest> updateArticle() {

        return List.of(
                DynamicTest.dynamicTest("정상 흐름", () -> {
                    ArticleRequest articleRequest = new ArticleRequest("updateTitle", "updateContent");

                    mockMvc.perform(put("/api/articles/{articleId}", 1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(articleRequest)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(201))
                            .andExpect(jsonPath("$.status").value("CREATED"))
                            .andExpect(jsonPath("$.message").value("CREATED"))
                            .andExpect(jsonPath("$.data.title").value("updateTitle"))
                            .andExpect(jsonPath("$.data.content").value("updateContent"))
                            .andDo(print());
                }),

                DynamicTest.dynamicTest("예외 상황, MethodArgumentNotValidException 발생 검증", () -> {

                    ArticleRequest articleRequest = new ArticleRequest("", "");

                    MvcResult result = mockMvc.perform(
                                    put("/api/articles/{articleId}", 1L)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(articleRequest)))
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.code").value(400))
                            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                            .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                            .andExpect(jsonPath("$.data").isEmpty())
                            .andDo(print())
                            .andReturn();

                    Exception exception = result.getResolvedException();
                    assertThat(exception).isNotNull();
                    assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
                })
        );
    }

    @DisplayName("DELETE /api/articles/{articleId} 테스트")
    @Test
    @WithPrincipalUser
    void deleteArticle() throws Exception {

        mockMvc.perform(delete("/api/articles/{articleId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @DisplayName("POST /api/articles/search 테스트")
    @ParameterizedTest(name = "검색 조건 : {0}")
    @MethodSource("createSearchRequests")
    void findAllArticlesByCond(SearchType searchType, ArticleSearchRequest request) throws Exception {

        User user = userRepository.findById(1L).get();
        User anotherUser = userRepository.findById(2L).get();
        List<Article> articles = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            LocalDateTime createdAt = LocalDateTime.now().minusDays(i);
            Article article = Article.builder()
                    .user((i % 2) == 0 ? user : anotherUser)
                    .content("content" + (i % 2))
                    .title("title" + (i % 2))
                    .build();
            article.setCreatedAt(createdAt);
            articles.add(article);
        }
        bulkInsertRepository.saveArticles(articles);

        mockMvc.perform(
                        post("/api/articles/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))

                .andExpect(jsonPath("$.data.dataList").isArray())
                .andExpect(jsonPath("$.data.dataList", hasSize(10)))
                .andExpect(jsonPath("$.data.totalCount").value(50));
    }

    @DisplayName("POST /api/articles/search 검색 조건 예외 테스트")
    @ParameterizedTest(name = "검색 조건 : {0}")
    @EnumSource(SearchType.class)
    void findAllArticlesByCondWithWrongRequest(SearchType searchType) throws Exception {

        ArticleSearchRequest request =
                new ArticleSearchRequest(searchType, null, null, null, null, null);

        MvcResult result = mockMvc.perform(
                        post("/api/articles/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 입력을 받았습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                .andReturn();

        Exception exception = result.getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(MethodArgumentNotValidException.class);
    }

    @DisplayName("GET /api/articles/page 테스트")
    @ParameterizedTest(name = "데이터 개수 : {0}, 페이지 번호 : {1}, 페이지 크기 : {2}")
    @ArgumentsSource(PageInfoData.class)
    void getPagingArticles(int dataSize, int currentPage, int pageSize) throws Exception {

        User user = userRepository.findById(1L).get();
        List<Article> articles = EasyRandomFactory.createArticles(dataSize, user);
        bulkInsertRepository.saveArticles(articles);

        mockMvc.perform(
                        get("/api/articles/page")
                                .param("page", String.valueOf(currentPage))
                                .param("size", String.valueOf(pageSize))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))

                .andExpect(jsonPath("$.data.dataList").isArray())
                .andExpect(jsonPath("$.data.dataList", hasSize(pageSize)));
    }

    @DisplayName("POST /api/articles/like/{articleId} 테스트")
    @TestFactory
    @WithPrincipalUser
    Collection<DynamicTest> addLike() {

        return List.of(
                DynamicTest.dynamicTest("정상 흐름", () -> {

                    mockMvc.perform(post("/api/articles/like/{articleId}", 1L))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200))
                            .andExpect(jsonPath("$.status").value("OK"))
                            .andExpect(jsonPath("$.message").value("OK"))
                            .andExpect(jsonPath("$.data").value(1));
                }),

                DynamicTest.dynamicTest("예외 흐름, 존재하지 않는 게시글 아이디 조회", () -> {

                    MvcResult result = mockMvc.perform(post("/api/articles/like/{articleId}", 999L))
                            .andDo(print())
                            .andExpect(status().isInternalServerError())
                            .andExpect(jsonPath("$.code").value(500))
                            .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                            .andExpect(jsonPath("$.message").value("INTERNAL_SERVER_ERROR"))
                            .andExpect(jsonPath("$.data").value("오류가 발생했습니다."))
                            .andReturn();

                    Exception exception = result.getResolvedException();
                    assertThat(exception).isNotNull();
                    assertThat(exception).isInstanceOf(EntityNotFoundException.class);
                })
        );
    }

    @DisplayName("POST /api/articles/like/{articleId} 테스트, 미인증 사용자는 요청할 수 없다.")
    @Test
    @WithAnonymousUser
    void addLikeAuthEx() throws Exception {

        mockMvc.perform(post("/api/articles/like/{articleId}", 1L))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/403"));
    }

    @DisplayName("DELETE /api/articles/like/{articleId} 테스트")
    @TestFactory
    @WithPrincipalUser
    Collection<DynamicTest> deleteLike() {

        return List.of(
                DynamicTest.dynamicTest("정상 흐름", () -> {

                    mockMvc.perform(delete("/api/articles/like/{articleId}", 1L))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200))
                            .andExpect(jsonPath("$.status").value("OK"))
                            .andExpect(jsonPath("$.message").value("OK"))
                            .andExpect(jsonPath("$.data").value(0));
                }),

                DynamicTest.dynamicTest("예외 흐름, 존재하지 않는 게시글 아이디 조회", () -> {

                    MvcResult result = mockMvc.perform(delete("/api/articles/like/{articleId}", 999L))
                            .andDo(print())
                            .andExpect(status().isInternalServerError())
                            .andExpect(jsonPath("$.code").value(500))
                            .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                            .andExpect(jsonPath("$.message").value("INTERNAL_SERVER_ERROR"))
                            .andExpect(jsonPath("$.data").value("오류가 발생했습니다."))
                            .andReturn();

                    Exception exception = result.getResolvedException();
                    assertThat(exception).isNotNull();
                    assertThat(exception).isInstanceOf(EntityNotFoundException.class);
                })
        );
    }

    @DisplayName("DELETE /api/articles/like/{articleId} 테스트, 미인증 사용자는 요청할 수 없다.")
    @Test
    @WithAnonymousUser
    void deleteLikeAuthEx() throws Exception {

        mockMvc.perform(delete("/api/articles/like/{articleId}", 1L))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/403"));
    }

    @DisplayName("GET /api/articles/{articleId}/liked 테스트")
    @TestFactory
    @WithPrincipalUser
    Collection<DynamicTest> isLiked() {

        return List.of(
                DynamicTest.dynamicTest("정상 흐름", () -> {

                    mockMvc.perform(get("/api/articles/{articleId}/liked", 1L))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200))
                            .andExpect(jsonPath("$.status").value("OK"))
                            .andExpect(jsonPath("$.message").value("OK"))
                            .andExpect(jsonPath("$.data.liked").value(true))
                            .andExpect(jsonPath("$.data.likesCount").value(1));
                }),

                DynamicTest.dynamicTest("예외 흐름, 존재하지 않는 게시글 아이디 조회", () -> {

                    MvcResult result = mockMvc.perform(get("/api/articles/{articleId}/liked", 999L))
                            .andDo(print())
                            .andExpect(status().isInternalServerError())
                            .andExpect(jsonPath("$.code").value(500))
                            .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                            .andExpect(jsonPath("$.message").value("INTERNAL_SERVER_ERROR"))
                            .andExpect(jsonPath("$.data").value("오류가 발생했습니다."))
                            .andReturn();

                    Exception exception = result.getResolvedException();
                    assertThat(exception).isNotNull();
                    assertThat(exception).isInstanceOf(EntityNotFoundException.class);
                })
        );
    }
}