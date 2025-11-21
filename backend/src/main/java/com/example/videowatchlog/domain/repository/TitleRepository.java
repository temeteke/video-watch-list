package com.example.videowatchlog.domain.repository;

import com.example.videowatchlog.domain.model.Title;
import java.util.List;
import java.util.Optional;

/**
 * TitleRepository インターフェース
 *
 * Title エンティティの永続化を管理します（リポジトリパターン）
 */
public interface TitleRepository {
    /**
     * IDで Title を検索します
     * @param id Title ID
     * @return Title オプション
     */
    Optional<Title> findById(Long id);

    /**
     * すべての Title を取得します
     * @return Title リスト
     */
    List<Title> findAll();

    /**
     * Title を保存します
     * @param title 保存する Title
     * @return 保存された Title
     */
    Title save(Title title);

    /**
     * IDで Title を削除します（カスケード削除）
     * @param id 削除する Title ID
     */
    void delete(Long id);

    /**
     * タイトル名が存在するかチェックします（大文字小文字区別なし）
     * @param name チェックするタイトル名
     * @return 存在する場合 true
     */
    boolean existsByName(String name);
}
