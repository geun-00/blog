package com.spring.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.blog.TestSecurityConfig;
import com.spring.blog.common.config.configs.WebMvcConfig;
import com.spring.blog.controller.api.BlogApiController;
import com.spring.blog.controller.api.UserApiController;
import com.spring.blog.mapper.ArticleMapper;
import com.spring.blog.mapper.UserMapper;
import com.spring.blog.service.BlogService;
import com.spring.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = {
                BlogApiController.class,
                UserApiController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class),
        })
@Import(TestSecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
public abstract class ApiControllerUnitTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserService userService;

    @MockBean
    protected UserMapper userMapper;

    @MockBean
    protected BlogService blogService;

    @MockBean
    protected ArticleMapper articleMapper;
}