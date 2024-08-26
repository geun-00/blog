package com.spring.blog.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ArticleSearchRequest {

    private String searchType;
    private String title;
    private String content;
    private String author;
    private TitleContent titleContent;
    private Period period;

    @Data
    private static class TitleContent {
        private String title;
        private String content;
    }

    @Data
    private static class Period{
        private LocalDate startDate;
        private LocalDate endDate;
    }
}