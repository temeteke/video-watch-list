package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Series;
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
    Optional<Series> findById(@Param("id") Long id);

    /**
     * タイトルIDでシリーズを検索します
     */
    List<Series> findByTitleId(@Param("titleId") Long titleId);

    /**
     * シリーズを挿入します（IDが自動設定される）
     */
    void insert(Series series);

    /**
     * シリーズを更新します
     */
    void update(Series series);

    /**
     * シリーズを削除します
     */
    void delete(@Param("id") Long id);
}
