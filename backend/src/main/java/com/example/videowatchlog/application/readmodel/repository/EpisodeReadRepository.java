package com.example.videowatchlog.application.readmodel.repository;

import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import java.util.Optional;

/**
 * EpisodeReadRepository インターフェース
 *
 * CQRS Read Model用のリポジトリインターフェースです。
 * Application層に配置され、読み取り専用のクエリを提供します。
 */
public interface EpisodeReadRepository {
    /**
     * エピソード詳細を取得（ViewingRecord を含む）
     *
     * @param seriesId シリーズID
     * @param episodeId エピソードID
     * @return エピソード詳細
     */
    Optional<EpisodeReadModel> findEpisodeDetailByIdAndSeriesId(Long seriesId, Long episodeId);
}
