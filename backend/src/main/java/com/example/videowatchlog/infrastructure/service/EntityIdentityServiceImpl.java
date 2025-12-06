package com.example.videowatchlog.infrastructure.service;

import com.example.videowatchlog.domain.service.EntityIdentityService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * EntityIdentityServiceImpl - エンティティID採番サービス実装
 *
 * PostgreSQL のシーケンスを使用して一意なIDを採番します。
 */
@Service
public class EntityIdentityServiceImpl implements EntityIdentityService {
    private final JdbcTemplate jdbcTemplate;

    public EntityIdentityServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * PostgreSQL の SERIAL シーケンスから次のIDを取得
     *
     * @return 採番されたID
     */
    @Override
    public Long generateId() {
        Long id = jdbcTemplate.queryForObject(
            "SELECT nextval('titles_id_seq')",
            Long.class
        );
        if (id == null) {
            throw new IllegalStateException("Failed to generate ID from sequence");
        }
        return id;
    }
}
