package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Episode;
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
    Optional<Episode> findById(@Param("id") Long id);

    /**
     * シリーズIDでエピソードを検索します
     */
    List<Episode> findBySeriesId(@Param("seriesId") Long seriesId);

    /**
     * エピソードを挿入します（IDが自動設定される）
     */
    void insert(Episode episode);

    /**
     * エピソードを更新します
     */
    void update(Episode episode);

    /**
     * エピソードを削除します
     */
    void delete(@Param("id") Long id);
}
