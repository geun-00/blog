package com.spring.blog.service;

import com.spring.blog.domain.Article;
import com.spring.blog.dto.AddArticleRequest;
import com.spring.blog.dto.UpdateArticleRequest;
import com.spring.blog.repository.BlogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    @DisplayName("블로그 글 저장에 성공한다.")
    @Test
    void addArticle() {

        // given
        final String title = "title";
        final String content = "content";

        AddArticleRequest request = new AddArticleRequest(title, content);

        //save() 메서드의 반환값 설정
        Article article = new Article(title, content);
        given(blogRepository.save(any(Article.class))).willReturn(article);

        // when
        Article savedArticle = blogService.save(request);

        // then
        assertThat(savedArticle.getTitle()).isEqualTo(title);
        assertThat(savedArticle.getContent()).isEqualTo(content);
        //save() 메서드가 1번 호출되었는지 확인
        verify(blogRepository, times(1)).save(any(Article.class));
    }

    @DisplayName("블로그 글 목록 조회에 성공한다.")
    @Test
    void findAll() {

        // given
        Article article1 = new Article("title1", "content1");
        Article article2 = new Article("title2", "content2");
        List<Article> articles = List.of(article1, article2);

        given(blogRepository.findAll()).willReturn(articles);

        // when
        List<Article> result = blogService.findAll();

        // then
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1"),
                        tuple("title2", "content2")
                );
    }

    @DisplayName("블로그 글 조회에 성공한다.")
    @Test
    void findArticle() {

        // given
        Long id = 1L;
        Article article = new Article("title", "content");

        given(blogRepository.findById(id)).willReturn(Optional.of(article));

        // when
        Article result = blogService.findById(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("title");
        assertThat(result.getContent()).isEqualTo("content");
    }

    @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다.")
    @Test
    void findArticleErrorByWrongId() {
        // given
        Long id = 1L;
        given(blogRepository.findById(id)).willReturn(Optional.empty());

        // when
        // then
        assertThatException().isThrownBy(() -> blogService.findById(id))
                .withMessage("not found : " + id);
    }

    @DisplayName("블로그 글 삭제에 성공한다.")
    @Test
    void deleteArticle() {

        // given
        long idToDel = 1L;
        Article article1 = new Article("title1", "content1");
        Article article2 = new Article("title2", "content2");

        List<Article> articlesBeforeDel = List.of(article1, article2);
        List<Article> articlesAfterDel = List.of(article2);

        given(blogRepository.findAll()).willReturn(articlesBeforeDel);

        // when
        List<Article> articles = blogService.findAll();

        // then
        assertThat(articles)
                .hasSize(2)
                .extracting("title", "content")
                .containsExactly(
                        tuple("title1", "content1"),
                        tuple("title2", "content2")
                );

        // when
        willDoNothing().given(blogRepository).deleteById(idToDel);

        blogService.delete(idToDel);
        then(blogRepository).should(times(1)).deleteById(idToDel);
        given(blogRepository.findAll()).willReturn(articlesAfterDel);

        articles = blogService.findAll();

        // then
        assertThat(articles)
                .hasSize(1)
                .extracting("title", "content")
                .containsExactly(
                        tuple("title2", "content2")
                );

    }

    @DisplayName("블로그 글 수정에 성공한다.")
    @Test
    void updateArticle() {

        // given
        long id = 1L;
        Article originalArticle = new Article("oldTitle", "oldContent");
        UpdateArticleRequest updateRequest = new UpdateArticleRequest("newTitle", "newContent");

        given(blogRepository.findById(id)).willReturn(Optional.of(originalArticle));

        // when
        Article updatedArticle = blogService.update(1, updateRequest);

        // then
        assertThat(updatedArticle).isNotNull()
                .extracting("title", "content")
                .containsExactly("newTitle", "newContent");

        then(blogRepository).should(times(1)).findById(id);
    }
}