package com.example.videowatchlog.infrastructure.service;

import com.example.videowatchlog.domain.service.TitleIdService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TitleIdServiceImpl - Title エンティティ用 ID采番サービス実装
 *
 * PostgreSQL の titles_id_seq シーケンスから一意なIDを采番します。
 */
@Service
public class TitleIdServiceImpl implements TitleIdService {
    private final JdbcTemplate jdbcTemplate;

    public TitleIdServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * PostgreSQL の SERIAL シーケンスから次のTitle IDを取得
     *
     * @return 采番されたID
     */
    @Override
    public Long generateId() {
        Long id = jdbcTemplate.queryForObject(
            "SELECT nextval('titles_id_seq')",
            Long.class
        );
        if (id == null) {
            throw new IllegalStateException("Failed to generate ID for Title from sequence titles_id_seq");
        }
        return id;
    }
}
