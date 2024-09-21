package com.spring.blog.parameterized;

import com.spring.blog.common.enums.SearchType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class SearchTypeWithPageInfo implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        List<? extends Arguments> pageInfos = new PageInfoData().provideArguments(context).toList();

        return Stream.of(SearchType.values())
                .flatMap(searchType -> pageInfos.stream().map(pageArgs ->
                        Arguments.of(searchType, pageArgs.get()[0], pageArgs.get()[1], pageArgs.get()[2])
                ));

    }
}
