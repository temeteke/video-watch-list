package com.example.videowatchlog.domain.repository;

import com.example.videowatchlog.domain.model.Series;
import java.util.List;
import java.util.Optional;

/**
 * SeriesRepository インターフェース
 *
 * Series エンティティの永続化を管理します（リポジトリパターン）
 */
public interface SeriesRepository {
    /**
     * IDで Series を検索します
     * @param id Series ID
     * @return Series オプション
     */
    Optional<Series> findById(Long id);

    /**
     * Title ID で Series を検索します
     * @param titleId Title ID
     * @return Series リスト
     */
    List<Series> findByTitleId(Long titleId);

    /**
     * Series を保存します
     * @param series 保存する Series
     * @return 保存された Series
     */
    Series save(Series series);

    /**
     * IDで Series を削除します（カスケード削除）
     * @param id 削除する Series ID
     */
    void delete(Long id);
}
