package com.example.videowatchlog.infrastructure.service;

import com.example.videowatchlog.domain.service.SeriesIdService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * SeriesIdServiceImpl - Series エンティティ用 ID采番サービス実装
 *
 * PostgreSQL の series_id_seq シーケンスから一意なIDを采番します。
 */
@Service
public class SeriesIdServiceImpl implements SeriesIdService {
    private final JdbcTemplate jdbcTemplate;

    public SeriesIdServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * PostgreSQL の SERIAL シーケンスから次のSeries IDを取得
     *
     * @return 采番されたID
     */
    @Override
    public Long generateId() {
        Long id = jdbcTemplate.queryForObject(
            "SELECT nextval('series_id_seq')",
            Long.class
        );
        if (id == null) {
            throw new IllegalStateException("Failed to generate ID for Series from sequence series_id_seq");
        }
        return id;
    }
}
