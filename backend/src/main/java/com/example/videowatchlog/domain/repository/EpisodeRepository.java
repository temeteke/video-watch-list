package com.example.videowatchlog.domain.repository;

import com.example.videowatchlog.domain.model.Episode;
import java.util.List;
import java.util.Optional;

/**
 * EpisodeRepository インターフェース
 *
 * Episode エンティティの永続化を管理します（リポジトリパターン）
 */
public interface EpisodeRepository {
    /**
     * IDで Episode を検索します
     * @param id Episode ID
     * @return Episode オプション
     */
    Optional<Episode> findById(Long id);

    /**
     * Series ID で Episode を検索します
     * @param seriesId Series ID
     * @return Episode リスト
     */
    List<Episode> findBySeriesId(Long seriesId);

    /**
     * Episode を保存します
     * @param episode 保存する Episode
     * @return 保存された Episode
     */
    Episode save(Episode episode);

    /**
     * IDで Episode を削除します（カスケード削除）
     * @param id 削除する Episode ID
     */
    void delete(Long id);
}
