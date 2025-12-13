package com.example.videowatchlog.application.readmodel.mapper;

import com.example.videowatchlog.application.readmodel.TitleDetailReadModel;
import com.example.videowatchlog.application.readmodel.TitleListReadModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TitleReadMapper - タイトル読み取り用 MyBatis マッパー
 *
 * Phase 7 CQRS パターン: Read Model Mapper
 * - JOIN を使用した効率的な SELECT
 * - N+1 クエリ問題を完全解決
 */
@Mapper
public interface TitleReadMapper {

    /**
     * すべてのタイトル一覧を取得（最小限の情報）
     *
     * @return タイトル一覧
     */
    @Select("SELECT id, name, created_at, updated_at FROM titles ORDER BY created_at DESC")
    List<TitleListReadModel> findAllTitles();

    /**
     * タイトル詳細の生データを取得（Series/Episode/ViewingRecord を含む）
     * 単一の JOIN クエリで全データを取得（返却はマップのリスト）
     * TitleReadService でこれを TitleDetailReadModel に変換する
     *
     * @param titleId タイトルID
     * @return タイトル詳細の生データ
     */
    List<Map<String, Object>> findTitleDetailByIdRaw(Long titleId);
}
