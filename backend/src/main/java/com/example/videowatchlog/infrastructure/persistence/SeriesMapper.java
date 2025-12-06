package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.infrastructure.persistence.entity.SeriesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

/**
 * SeriesMapper - MyBatis マッパーインターフェース
 */
@Mapper
public interface SeriesMapper {

    /**
     * IDでシリーズを検索します
     */
    Optional<SeriesEntity> findById(@Param("id") Long id);

    /**
     * タイトルIDでシリーズを検索します
     */
    List<SeriesEntity> findByTitleId(@Param("titleId") Long titleId);

    /**
     * シリーズを挿入します（IDが自動設定される）
     */
    void insert(SeriesEntity seriesEntity);

    /**
     * シリーズを更新します
     */
    void update(SeriesEntity seriesEntity);

    /**
     * シリーズを削除します
     */
    void delete(@Param("id") Long id);
}
