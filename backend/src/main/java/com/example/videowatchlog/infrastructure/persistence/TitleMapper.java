package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.Title;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

/**
 * TitleMapper - MyBatis マッパーインターフェース
 *
 * Title エンティティの永続化操作を定義します
 */
@Mapper
public interface TitleMapper {

    /**
     * IDでタイトルを検索します
     * @param id タイトルID
     * @return タイトル（存在しない場合はEmpty）
     */
    Optional<Title> findById(@Param("id") Long id);

    /**
     * すべてのタイトルを取得します
     * @return タイトルリスト
     */
    List<Title> findAll();

    /**
     * タイトルを挿入します
     * @param title 挿入するタイトル（IDが自動設定される）
     */
    void insert(Title title);

    /**
     * タイトルを更新します
     * @param title 更新するタイトル
     */
    void update(Title title);

    /**
     * タイトルを削除します（カスケード削除）
     * @param id 削除するタイトルID
     */
    void delete(@Param("id") Long id);

    /**
     * タイトル名で存在確認します
     * @param name チェックするタイトル名
     * @return 存在する場合true
     */
    boolean existsByName(@Param("name") String name);
}
