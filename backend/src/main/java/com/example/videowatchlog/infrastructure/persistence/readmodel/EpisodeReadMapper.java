package com.example.videowatchlog.infrastructure.persistence.readmodel;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * EpisodeReadMapper - エピソード読み取り用 MyBatis Mapper
 *
 * 読み取り専用のクエリを提供します。
 * Infrastructure層の詳細（MyBatis XML）への依存性を抽象化します。
 */
@Mapper
public interface EpisodeReadMapper {
    /**
     * エピソード詳細と関連する全データを取得（raw 形式）
     *
     * @param seriesId シリーズID
     * @param episodeId エピソードID
     * @return エピソード、watch_page_urls、視聴記録を含む JOIN クエリの結果
     */
    List<Map<String, Object>> findEpisodeDetailByIdAndSeriesIdRaw(
        @Param("seriesId") Long seriesId,
        @Param("episodeId") Long episodeId
    );
}
