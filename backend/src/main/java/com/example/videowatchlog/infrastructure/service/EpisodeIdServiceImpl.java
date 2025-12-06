package com.example.videowatchlog.infrastructure.service;

import com.example.videowatchlog.domain.service.EpisodeIdService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * EpisodeIdServiceImpl - Episode エンティティ用 ID采番サービス実装
 *
 * PostgreSQL の episodes_id_seq シーケンスから一意なIDを采番します。
 */
@Service
public class EpisodeIdServiceImpl implements EpisodeIdService {
    private final JdbcTemplate jdbcTemplate;

    public EpisodeIdServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * PostgreSQL の SERIAL シーケンスから次のEpisode IDを取得
     *
     * @return 采番されたID
     */
    @Override
    public Long generateId() {
        Long id = jdbcTemplate.queryForObject(
            "SELECT nextval('episodes_id_seq')",
            Long.class
        );
        if (id == null) {
            throw new IllegalStateException("Failed to generate ID for Episode from sequence episodes_id_seq");
        }
        return id;
    }
}
