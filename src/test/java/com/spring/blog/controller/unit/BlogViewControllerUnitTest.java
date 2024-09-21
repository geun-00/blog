package com.spring.blog.controller.unit;

import com.spring.blog.EasyRandomFactory;
import com.spring.blog.common.argumentResolver.UserKeyArgumentResolver;
import com.spring.blog.common.enums.SearchType;
import com.spring.blog.controller.ViewControllerUnitTestSupport;
import com.spring.blog.controller.view.BlogViewController;
import com.spring.blog.parameterized.PageInfoData;
import com.spring.blog.service.dto.response.AddArticleViewResponse;
import com.spring.blog.service.dto.response.ArticleListViewResponse;
import com.spring.blog.service.dto.response.ArticleViewResponse;
import com.spring.blog.service.dto.response.PageResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("블로그 뷰 단위 테스트")
class BlogViewControllerUnitTest extends ViewControllerUnitTestSupport {

    @DisplayName("/guest 테스트, 쿠키와 세션이 모두 무효화되며 /articles로 리다이렉션 된다.")
    @Test
    @WithAnonymousUser
    void guest() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        Cookie cookie = new Cookie("JSESSIONID", session.getId());

        mockMvc.perform(get("/guest")
                        .session(session)
                        .cookie(cookie)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles"))

                .andExpect(result -> {
                    HttpSession httpSession = result.getRequest().getSession(false);
                    assertThat(httpSession).isNull();
                })

                .andExpect(cookie().maxAge("JSESSIONID", 0))
                .andExpect(cookie().path("JSESSIONID", "/"))

                .andDo(print());
    }

    @DisplayName("/getArticles 테스트, 모델에 정확한 데이터와 페이지 정보가 담겨야 한다.")
    @ParameterizedTest(name = "데이터 개수 : {0}, 페이지 번호 : {1}, 페이지 크기 : {2}")
    @ArgumentsSource(PageInfoData.class)
    void getArticles(int dataSize, int currentPage, int pageSize) throws Exception {

        // given
        PageResponse<ArticleListViewResponse> pageResponse =
                EasyRandomFactory.createPageResponse(dataSize, currentPage, pageSize);

        given(blogService.findAll(any(Pageable.class))).willReturn(pageResponse);

        // when
        MvcResult result = mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("articleList"))
                .andExpect(model().attributeExists("articles", "currentDate", "searchType", "page"))
                .andDo(print())
                .andReturn();

        // then
        Map<String, Object> model = result.getModelAndView().getModel();

        Object searchTypeData = model.get("searchType");
        assertThat(searchTypeData).isInstanceOf(SearchType[].class);
        SearchType[] searchTypes = (SearchType[]) searchTypeData;
        assertThat(searchTypes).containsExactlyInAnyOrder(SearchType.values());

        Object articleData = model.get("articles");
        assertThat(articleData).isInstanceOf(List.class);
        List<?> articles = (List<?>) articleData;
        assertThat(articles).isNotNull();
        assertThat(articles).hasSize(pageSize);
        assertThat(articles.get(0)).isInstanceOf(ArticleListViewResponse.class);

        Object currentDateData = model.get("currentDate");
        assertThat(currentDateData).isInstanceOf(LocalDate.class);
        LocalDate currentDate = (LocalDate) currentDateData;
        assertThat(currentDate).isNotNull();
        assertThat(currentDate).isToday();

        Object pageData = model.get("page");
        assertThat(pageData).isInstanceOf(PageResponse.class);
        PageResponse<?> page = (PageResponse<?>) pageData;
        assertThat(page).isNotNull();
        assertThat(page.getDataList()).hasSize(pageSize);

        int end = (int) (Math.ceil(currentPage / 10.0)) * 10;
        int start = end - 9;

        int last = (int) Math.ceil(dataSize / (double) pageSize);
        end = Math.min(end, last);

        List<Integer> pageNumList = IntStream.rangeClosed(start, end).boxed().toList();

        assertThat(page.getPageNumList()).containsExactlyElementsOf(pageNumList);
        assertThat(page.getCurrentPage()).isEqualTo(currentPage);
        assertThat(page.getTotalCount()).isEqualTo(dataSize);

        boolean expectedPrev = start > 1;
        boolean expectedNext = dataSize > end * pageSize;

        assertThat(page.isPrev()).isEqualTo(expectedPrev);
        assertThat(page.isNext()).isEqualTo(expectedNext);
        assertThat(page.getPrevPage()).isEqualTo(expectedPrev ? start - 1 : 0);
        assertThat(page.getNextPage()).isEqualTo(expectedNext ? end + 1 : 0);
    }

    @DisplayName("/articles/{id} 테스트")
    @TestFactory
    @WithAnonymousUser
    Collection<DynamicTest> getArticle() {

        mockMvc = MockMvcBuilders.standaloneSetup(new BlogViewController(blogService))
                .setCustomArgumentResolvers(new UserKeyArgumentResolver())
                .build();

        // given
        Long articleId = 1L;
        ArticleViewResponse response = EasyRandomFactory.createArticleViewResponse(articleId);

        given(blogService.getArticleAndIncreaseViews(any(Long.class), any(String.class))).willReturn(response);

        return List.of(
                DynamicTest.dynamicTest("쿠키가 없을 때 USER_KEY 생성", () ->

                        mockMvc.perform(get("/articles/{id}", articleId))
                                .andExpect(status().isOk())
                                .andExpect(cookie().exists("USER_KEY"))
                                .andExpect(cookie().maxAge("USER_KEY", 60 * 60 * 24))
                                .andExpect(cookie().path("USER_KEY", "/"))
                                .andDo(print()))
                ,

                DynamicTest.dynamicTest("쿠키가 있을 때 USER_KEY 사용", () -> {

                    String userKeyValue = "test-user-key";

                    MvcResult result = mockMvc.perform(
                                    get("/articles/{id}", articleId)
                                            .cookie(new Cookie("USER_KEY", userKeyValue))
                            )
                            .andExpect(status().isOk())
                            .andExpect(view().name("article"))
                            .andExpect(model().attributeExists("article"))
                            .andDo(print()).andReturn();

                    Cookie[] cookies = result.getRequest().getCookies();
                    assertThat(cookies).isNotNull();
                    assertThat(cookies).contains(new Cookie("USER_KEY", userKeyValue));
                })
                ,

                DynamicTest.dynamicTest("모델 데이터 검증", () -> {

                    MvcResult result = mockMvc.perform(get("/articles/{id}", articleId))
                            .andExpect(status().isOk())
                            .andExpect(view().name("article"))
                            .andExpect(model().attributeExists("article"))
                            .andDo(print())
                            .andReturn();

                    Map<String, Object> model = result.getModelAndView().getModel();
                    Object data = model.get("article");

                    assertThat(data).isInstanceOf(ArticleViewResponse.class);

                    ArticleViewResponse article = (ArticleViewResponse) data;
                    assertThat(article).isEqualTo(response);
                })
        );
    }

    @DisplayName("/new-article 테스트")
    @TestFactory
    @WithMockUser
    Collection<DynamicTest> new_article() {

        return List.of(

                DynamicTest.dynamicTest("id 없을 때, 빈 모델을 담고 등록 버튼이 보인다.", () -> {

                    MvcResult result = mockMvc.perform(get("/new-article"))
                            .andExpect(status().isOk())
                            .andExpect(view().name("newArticle"))
                            .andExpect(model().attributeExists("article"))
                            .andDo(print())
                            .andReturn();

                    Map<String, Object> model = result.getModelAndView().getModel();

                    Object article = model.get("article");
                    assertThat(article).isInstanceOf(AddArticleViewResponse.class);

                    AddArticleViewResponse response = (AddArticleViewResponse) article;

                    assertThat(response.title()).isNull();
                    assertThat(response.content()).isNull();
                    assertThat(response.id()).isNull();

                    String content = result.getResponse().getContentAsString();
                    Document document = Jsoup.parse(content);

                    Element modifyBtn = document.selectFirst("#modify-btn");
                    assertThat(modifyBtn).isNull();

                    Element createBtn = document.selectFirst("#create-btn");
                    assertThat(createBtn).isNotNull();
                    assertThat(createBtn.text()).isEqualTo("등록");
                }),

                DynamicTest.dynamicTest("id 있을 때, 모델을 담고 수정 버튼이 보인다.", () -> {

                    Long id = 1L;

                    AddArticleViewResponse addArticleViewResponse = EasyRandomFactory.createAddArticleViewResponse();

                    given(blogService.getAddArticleViewResponse(id)).willReturn(addArticleViewResponse);

                    MvcResult result = mockMvc.perform(
                            get("/new-article")
                                    .param("id", String.valueOf(id))
                            )
                            .andExpect(status().isOk())
                            .andExpect(view().name("newArticle"))
                            .andExpect(model().attributeExists("article"))
                            .andDo(print())
                            .andReturn();

                    Map<String, Object> model = result.getModelAndView().getModel();

                    Object article = model.get("article");
                    assertThat(article).isInstanceOf(AddArticleViewResponse.class);

                    AddArticleViewResponse response = (AddArticleViewResponse) article;

                    assertThat(response.title()).isNotNull();
                    assertThat(response.content()).isNotNull();
                    assertThat(response.id()).isNotNull();

                    String content = result.getResponse().getContentAsString();
                    Document document = Jsoup.parse(content);

                    Element createBtn = document.selectFirst("#create-btn");
                    assertThat(createBtn).isNull();

                    Element modifyBtn = document.selectFirst("#modify-btn");
                    assertThat(modifyBtn).isNotNull();
                    assertThat(modifyBtn.text()).isEqualTo("수정");
                })
        );
    }
}