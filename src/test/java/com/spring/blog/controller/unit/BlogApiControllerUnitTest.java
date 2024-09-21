package com.spring.blog.controller.unit;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.annotation.WithPrincipalUser;
import com.spring.blog.common.argumentResolver.CurrentUserArgumentResolver;
import com.spring.blog.common.enums.SearchType;
import com.spring.blog.controller.ApiControllerUnitTestSupport;
import com.spring.blog.controller.advice.ApiControllerAdvice;
import com.spring.blog.controller.api.BlogApiController;
import com.spring.blog.controller.dto.request.ArticleRequest;
import com.spring.blog.controller.dto.request.ArticleSearchRequest;
import com.spring.blog.parameterized.PageInfoData;
import com.spring.blog.parameterized.SearchTypeWithPageInfo;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import com.spring.blog.service.dto.request.ArticleServiceRequest;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.ArticleResponse;
import com.spring.blog.service.dto.response.LikeResponse;
import com.spring.blog.service.dto.response.PageResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("블로그 API 단위 테스트")
public class BlogApiControllerUnitTest extends ApiControllerUnitTestSupport {

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BlogApiController(blogService, articleMapper))
                .setCustomArgumentResolvers(
                        new CurrentUserArgumentResolver(),
                        new PageableHandlerMethodArgumentResolver()
                )
                .setControllerAdvice(new ApiControllerAdvice())
                .build();
    }

    @DisplayName("POST /api/articles 테스트")
    @TestFactory
    @WithPrincipalUser
    Collection<DynamicTest> addArticle() {

        return List.of(
                DynamicTest.dynamicTest("정상 흐름", () -> {

                    MockHttpSession session = new MockHttpSession();
                    Cookie cookie = new Cookie("JSESSIONID", session.getId());

                    ArticleRequest articleRequest = new ArticleRequest("title", "content");
                    ArticleServiceRequest articleServiceRequest = new ArticleServiceRequest("title", "content");

                    given(articleMapper.toServiceRequest(articleRequest)).willReturn(articleServiceRequest);
                    given(blogService.save(any(ArticleServiceRequest.class), anyString(), anyString()))
                            .willReturn(new ArticleResponse("title", "content"));

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
                    ArticleServiceRequest articleServiceRequest = new ArticleServiceRequest("updateTitle", "updateContent");

                    given(articleMapper.toServiceRequest(articleRequest)).willReturn(articleServiceRequest);
                    given(blogService.update(any(ArticleServiceRequest.class), anyLong()))
                            .willReturn(new ArticleResponse("updateTitle", "updateContent"));

                    mockMvc.perform(
                                    put("/api/articles/{articleId}", 1L)
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
    void deleteArticle() throws Exception {

        mockMvc.perform(delete("/api/articles/{articleId}", anyLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        verify(blogService, times(1)).delete(anyLong());
    }

    @DisplayName("POST /api/articles/search 테스트")
    @ParameterizedTest(name = "검색 조건 : {0}, 데이터 개수 : {1}, 페이지 번호 : {2}, 페이지 크기 : {3}")
    @ArgumentsSource(SearchTypeWithPageInfo.class)
    void findAllArticlesByCond(SearchType searchType, int dataSize, int currentPage, int pageSize) throws Exception {

        PageResponse<ArticleListViewResponse> pageResponse =
                EasyRandomFactory.createPageResponse(dataSize, currentPage, pageSize);

        ArticleSearchServiceRequest articleSearchServiceRequest =
                EasyRandomFactory.createArticleSearchServiceRequest(searchType);

        ArticleSearchRequest request = EasyRandomFactory.createArticleSearchRequest(searchType);

        given(articleMapper.toServiceRequest(any(ArticleSearchRequest.class)))
                .willReturn(articleSearchServiceRequest);

        given(blogService.findAllByCond(any(ArticleSearchServiceRequest.class), any(Pageable.class)))
                .willReturn(pageResponse);

        int end = (int) (Math.ceil(currentPage / 10.0)) * 10;
        int start = end - 9;

        int last = (int) Math.ceil(dataSize / (double) pageSize);

        end = Math.min(end, last);

        List<Integer> pageNumList = IntStream.rangeClosed(start, end).boxed().toList();

        boolean expectedPrev = start > 1;
        boolean expectedNext = dataSize > end * pageSize;

        mockMvc.perform(
                        post("/api/articles/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))

                .andExpect(jsonPath("$.data.dataList").isArray())
                .andExpect(jsonPath("$.data.dataList", hasSize(pageSize)))

                .andExpect(jsonPath("$.data.pageNumList").isArray())
                .andExpect(jsonPath("$.data.pageNumList", hasSize(pageNumList.size())))
                .andExpect(jsonPath("$.data.pageNumList[*]").value(containsInAnyOrder(pageNumList.toArray())))

                .andExpect(jsonPath("$.data.prev").isBoolean())
                .andExpect(jsonPath("$.data.prev").value(expectedPrev))

                .andExpect(jsonPath("$.data.next").isBoolean())
                .andExpect(jsonPath("$.data.next").value(expectedNext))

                .andExpect(jsonPath("$.data.totalCount").value(dataSize))
                .andExpect(jsonPath("$.data.currentPage").value(currentPage))

                .andExpect(jsonPath("$.data.prevPage").value(expectedPrev ? start - 1 : 0))
                .andExpect(jsonPath("$.data.nextPage").value(expectedNext ? end + 1 : 0));

        verify(blogService, times(1))
                .findAllByCond(any(ArticleSearchServiceRequest.class), any(Pageable.class));

        verify(articleMapper, times(1)).toServiceRequest(any(ArticleSearchRequest.class));
    }

    @DisplayName("GET /api/articles/page 테스트")
    @ParameterizedTest(name = "데이터 개수 : {0}, 페이지 번호 : {1}, 페이지 크기 : {2}")
    @ArgumentsSource(PageInfoData.class)
    void getPagingArticles(int dataSize, int currentPage, int pageSize) throws Exception {

        PageResponse<ArticleListViewResponse> pageResponse =
                EasyRandomFactory.createPageResponse(dataSize, currentPage, pageSize);

        given(blogService.findAll(any(Pageable.class))).willReturn(pageResponse);

        int end = (int) (Math.ceil(currentPage / 10.0)) * 10;
        int start = end - 9;

        int last = (int) Math.ceil(dataSize / (double) pageSize);

        end = Math.min(end, last);

        List<Integer> pageNumList = IntStream.rangeClosed(start, end).boxed().toList();

        boolean expectedPrev = start > 1;
        boolean expectedNext = dataSize > end * pageSize;

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
                .andExpect(jsonPath("$.data.dataList", hasSize(pageSize)))

                .andExpect(jsonPath("$.data.pageNumList").isArray())
                .andExpect(jsonPath("$.data.pageNumList", hasSize(pageNumList.size())))
                .andExpect(jsonPath("$.data.pageNumList[*]").value(containsInAnyOrder(pageNumList.toArray())))

                .andExpect(jsonPath("$.data.prev").isBoolean())
                .andExpect(jsonPath("$.data.prev").value(expectedPrev))

                .andExpect(jsonPath("$.data.next").isBoolean())
                .andExpect(jsonPath("$.data.next").value(expectedNext))

                .andExpect(jsonPath("$.data.totalCount").value(dataSize))
                .andExpect(jsonPath("$.data.currentPage").value(currentPage))

                .andExpect(jsonPath("$.data.prevPage").value(expectedPrev ? start - 1 : 0))
                .andExpect(jsonPath("$.data.nextPage").value(expectedNext ? end + 1 : 0));

        verify(blogService, times(1))
                .findAll(any(Pageable.class));
    }

    @DisplayName("POST /api/articles/like/{articleId} 테스트")
    @Test
    @WithPrincipalUser
    void addLike() throws Exception {

        int returnValue = 5;
        given(blogService.addLike(anyLong(), anyString())).willReturn(returnValue);

        mockMvc.perform(post("/api/articles/like/{articleId}", 1L))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value(returnValue));
    }

    @DisplayName("DELETE /api/articles/like/{articleId} 테스트")
    @Test
    @WithPrincipalUser
    void deleteLike() throws Exception {

        int returnValue = 5;
        given(blogService.deleteLike(anyLong(), anyString())).willReturn(returnValue);

        mockMvc.perform(delete("/api/articles/like/{articleId}", 1L))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value(returnValue));
    }

    @DisplayName("GET /api/articles/{articleId}/liked 테스트")
    @Test
    void isLiked() throws Exception {

        boolean returnLiked = true;
        int returnLikes = 3;
        given(blogService.isLiked(anyLong(), nullable(String.class))).willReturn(new LikeResponse(returnLiked, returnLikes));

        mockMvc.perform(get("/api/articles/{articleId}/liked", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.liked").value(returnLiked))
                .andExpect(jsonPath("$.data.likesCount").value(returnLikes));

        verify(blogService, times(1)).isLiked(anyLong(), nullable(String.class));
    }
}