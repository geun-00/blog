package com.spring.blog.dto;

import com.spring.blog.common.enums.SearchType;
import lombok.Data;

import java.time.LocalDate;

@Data
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
}