package com.spring.blog.parameterized;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class PageInfoData implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                //dataSize, currentPage, pageSize
                Arguments.of(50, 1, 5),
                Arguments.of(75, 3, 10),
                Arguments.of(150, 5, 20),
                Arguments.of(300, 7, 30),
                Arguments.of(500, 9, 40)
        );
    }
}