package com.spring.blog.service.dto.request;

import com.spring.blog.common.enums.SearchType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ArticleSearchServiceRequest {

    private SearchType searchType;
    private String title;
    private String content;
    private String author;
    private TitleContent titleContent;
    private Period period;

    @Data
    @Builder
    public static class TitleContent {
        private String title;
        private String content;
    }

    @Data
    @Builder
    public static class Period {
        private LocalDate startDate;
        private LocalDate endDate;
    }
}