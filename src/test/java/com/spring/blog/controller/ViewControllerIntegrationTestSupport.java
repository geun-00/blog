package com.spring.blog.controller;

import com.spring.blog.IntegrationTestSupport;
import com.spring.blog.repository.ArticleLikesRepository;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.BulkInsertRepository;
import com.spring.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class ViewControllerIntegrationTestSupport extends IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BlogRepository blogRepository;

    @Autowired
    protected ArticleLikesRepository articleLikesRepository;

    @Autowired
    protected BulkInsertRepository bulkInsertRepository;
}
