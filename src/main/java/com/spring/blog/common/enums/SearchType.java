package com.spring.blog.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {

    TITLE("title", "제목"),
    CONTENT("content", "내용"),
    AUTHOR("author", "작성자"),
    TITLE_CONTENT("title-content", "제목 + 내용"),
    PERIOD("period", "기간"),
    VIEWS("views", "조회수");

    private final String description_en;
    private final String description_kr;

    @JsonCreator
    public static SearchType of(String value) {
        for (SearchType type : values()) {
            if (type.getDescription_en().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SearchType value: " + value);
    }
}