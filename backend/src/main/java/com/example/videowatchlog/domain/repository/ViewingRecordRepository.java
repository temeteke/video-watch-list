package com.example.videowatchlog.domain.repository;

import com.example.videowatchlog.domain.model.ViewingRecord;
import java.util.Optional;

/**
 * ViewingRecordRepository インターフェース
 *
 * ViewingRecord エンティティの永続化を管理します（リポジトリパターン）
 *
 * Note: ViewingRecord is part of the Episode aggregate.
 * While this repository provides access to individual records,
 * modifications should typically be done through the Episode aggregate root.
 */
public interface ViewingRecordRepository {
    /**
     * IDで ViewingRecord を検索します
     *
     * @param id ViewingRecord ID
     * @return ViewingRecord オプション
     */
    Optional<ViewingRecord> findById(Long id);

    /**
     * ViewingRecord を削除します
     *
     * @param id 削除する ViewingRecord ID
     */
    void delete(Long id);
}
