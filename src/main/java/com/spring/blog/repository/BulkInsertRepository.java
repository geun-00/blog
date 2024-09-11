package com.spring.blog.repository;

import com.spring.blog.domain.ArticleImages;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BulkInsertRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveArticleImages(List<ArticleImages> images) {

        jdbcTemplate.batchUpdate(
                "INSERT INTO ARTICLE_IMAGES (ARTICLE_ID, IMAGE_URL) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, images.get(i).getArticle().getId());
                        ps.setString(2, images.get(i).getImageUrl());
                    }

                    @Override
                    public int getBatchSize() {
                        return images.size();
                    }
                }
        );
    }
}