package com.spring.blog.controller.integration;

import com.amazonaws.HttpMethod;
import com.spring.blog.controller.ApiControllerIntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("테스트 환경 파일 API 통합 테스트")
class LocalFileControllerTest extends ApiControllerIntegrationTestSupport {

    @Value("${file.dir}")
    private String dir;

    @DisplayName("GET /user/defaultImage 테스트")
    @Test
    @WithMockUser
    void defaultUserImage() throws Exception {
        mockMvc.perform(get("/user/defaultImage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("GET /{filename} 테스트")
    @Test
    @WithMockUser
    void uploadUserImage() throws Exception {
        mockMvc.perform(get("/user/{filename}", "default_user.png"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("PUT /article/{filename} 테스트")
    @Test
    @WithMockUser
    void putUploadArticleImage() throws Exception {

        String filename = "test_image.png";
        MockMultipartFile file = new MockMultipartFile(
                "file", filename, "image/png", "image data".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/article/{filename}", filename)
                        .file(file)
                        .with(request -> {
                            request.setMethod(HttpMethod.PUT.name());
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String filePath = "/article/" + filename;
        assertThat(content).isEqualTo(filePath);

        cleanup(filePath);
    }

    @DisplayName("GET /article/{filename} 테스트")
    @Test
    @WithMockUser
    void getUploadArticleImage() throws Exception {

        String filename = "test_image.png";
        MockMultipartFile file = new MockMultipartFile(
                "file", filename, "image/png", "image data".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/article/{filename}", filename)
                        .file(file)
                        .with(request -> {
                            request.setMethod(HttpMethod.PUT.name());
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        mockMvc.perform(get(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));

        cleanup(content);
    }

    private void cleanup(String filePath) throws IOException {
        Path path = Paths.get(dir + filePath);

        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

}