package com.example.videowatchlog.domain.repository;

import com.example.videowatchlog.domain.model.ViewingRecord;
import java.util.List;
import java.util.Optional;

/**
 * ViewingRecordRepository インターフェース
 *
 * ViewingRecord エンティティの永続化を管理します（リポジトリパターン）
 */
public interface ViewingRecordRepository {
    /**
     * IDで ViewingRecord を検索します
     * @param id ViewingRecord ID
     * @return ViewingRecord オプション
     */
    Optional<ViewingRecord> findById(Long id);

    /**
     * Episode ID で ViewingRecord を検索します
     * @param episodeId Episode ID
     * @return ViewingRecord リスト（新しい順）
     */
    List<ViewingRecord> findByEpisodeId(Long episodeId);

    /**
     * ViewingRecord を保存します
     * @param viewingRecord 保存する ViewingRecord
     * @return 保存された ViewingRecord
     */
    ViewingRecord save(ViewingRecord viewingRecord);

    /**
     * IDで ViewingRecord を削除します
     * @param id 削除する ViewingRecord ID
     */
    void delete(Long id);
}
