package com.spring.blog.controller;

import com.spring.blog.TestSecurityConfig;
import com.spring.blog.common.config.configs.WebMvcConfig;
import com.spring.blog.controller.view.BlogViewController;
import com.spring.blog.controller.view.UserViewController;
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
                BlogViewController.class,
                UserViewController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class),
        })
@Import(TestSecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
public abstract class ViewControllerUnitTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected BlogService blogService;

    @MockBean
    protected UserService userService;
}