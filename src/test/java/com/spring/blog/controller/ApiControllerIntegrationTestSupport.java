package com.spring.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.blog.BlogApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = BlogApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class ApiControllerIntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}