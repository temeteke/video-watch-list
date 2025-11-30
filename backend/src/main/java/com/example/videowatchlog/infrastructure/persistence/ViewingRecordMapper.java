package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.ViewingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

/**
 * ViewingRecordMapper - MyBatis マッパーインターフェース
 */
@Mapper
public interface ViewingRecordMapper {

    /**
     * IDで視聴履歴を検索します
     */
    Optional<ViewingRecord> findById(@Param("id") Long id);

    /**
     * エピソードIDで視聴履歴を検索します（新しい順）
     */
    List<ViewingRecord> findByEpisodeId(@Param("episodeId") Long episodeId);

    /**
     * 視聴履歴を挿入します（IDが自動設定される）
     */
    void insert(ViewingRecord viewingRecord);

    /**
     * 視聴履歴を削除します
     */
    void delete(@Param("id") Long id);
}
