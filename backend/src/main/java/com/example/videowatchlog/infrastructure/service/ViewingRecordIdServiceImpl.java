package com.example.videowatchlog.infrastructure.service;

import com.example.videowatchlog.domain.service.ViewingRecordIdService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * ViewingRecordIdServiceImpl - ViewingRecord エンティティ用 ID采番サービス実装
 *
 * PostgreSQL の viewing_records_id_seq シーケンスから一意なIDを采番します。
 */
@Service
public class ViewingRecordIdServiceImpl implements ViewingRecordIdService {
    private final JdbcTemplate jdbcTemplate;

    public ViewingRecordIdServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * PostgreSQL の SERIAL シーケンスから次のViewingRecord IDを取得
     *
     * @return 采番されたID
     */
    @Override
    public Long generateId() {
        Long id = jdbcTemplate.queryForObject(
            "SELECT nextval('viewing_records_id_seq')",
            Long.class
        );
        if (id == null) {
            throw new IllegalStateException("Failed to generate ID for ViewingRecord from sequence viewing_records_id_seq");
        }
        return id;
    }
}
