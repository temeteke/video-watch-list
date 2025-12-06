package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.infrastructure.persistence.entity.EpisodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

/**
 * EpisodeMapper - MyBatis マッパーインターフェース
 */
@Mapper
public interface EpisodeMapper {

    /**
     * IDでエピソードを検索します
     */
    Optional<EpisodeEntity> findById(@Param("id") Long id);

    /**
     * シリーズIDでエピソードを検索します
     */
    List<EpisodeEntity> findBySeriesId(@Param("seriesId") Long seriesId);

    /**
     * エピソードを挿入します（IDが自動設定される）
     */
    void insert(EpisodeEntity episodeEntity);

    /**
     * エピソードを更新します
     */
    void update(EpisodeEntity episodeEntity);

    /**
     * エピソードを削除します
     */
    void delete(@Param("id") Long id);

    /**
     * エピソードIDで視聴ページURLを取得します
     */
    List<com.example.videowatchlog.domain.model.WatchPageUrl> selectWatchPageUrlsByEpisodeId(@Param("id") Long episodeId);

    /**
     * 視聴ページURLを挿入します
     */
    void insertWatchPageUrl(@Param("episodeId") Long episodeId, @Param("url") String url);

    /**
     * エピソードIDで視聴ページURLを全削除します
     */
    void deleteWatchPageUrlsByEpisodeId(@Param("episodeId") Long episodeId);
}
