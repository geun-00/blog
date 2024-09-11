package com.spring.blog.dto.request;

import com.spring.blog.common.annotation.ValidArticleSearchRequest;
import com.spring.blog.common.enums.SearchType;
import com.spring.blog.service.dto.request.ArticleSearchServiceRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
@ValidArticleSearchRequest
public class ArticleSearchRequest {

    private SearchType searchType;
    private String title;
    private String content;
    private String author;
    private TitleContent titleContent;
    private Period period;

    @Data
    public static class TitleContent {
        private String title;
        private String content;
    }

    @Data
    public static class Period {
        private LocalDate startDate;
        private LocalDate endDate;
    }

    public ArticleSearchServiceRequest toServiceRequest() {
        return ArticleSearchServiceRequest.builder()
                .searchType(searchType)
                .title(title)
                .content(content)
                .author(author)
                .titleContent(ArticleSearchServiceRequest.TitleContent.builder()
                        .title(getTitleContent().getTitle())
                        .content(getTitleContent().getContent())
                        .build())
                .period(ArticleSearchServiceRequest.Period.builder()
                        .startDate(getPeriod().getStartDate())
                        .endDate(getPeriod().getEndDate())
                        .build())
                .build();
    }
}