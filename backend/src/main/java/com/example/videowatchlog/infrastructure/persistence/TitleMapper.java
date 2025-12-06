package com.example.videowatchlog.infrastructure.persistence;

import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.infrastructure.persistence.entity.TitleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

/**
 * TitleMapper - MyBatis マッパーインターフェース
 *
 * TitleEntity の永続化操作を定義します
 */
@Mapper
public interface TitleMapper {

    /**
     * IDでタイトルを検索します
     * @param id タイトルID
     * @return タイトルエンティティ（存在しない場合はEmpty）
     */
    Optional<TitleEntity> findById(@Param("id") Long id);

    /**
     * すべてのタイトルを取得します
     * @return タイトルエンティティリスト
     */
    List<TitleEntity> findAll();

    /**
     * タイトルを挿入します
     * @param titleEntity 挿入するタイトルエンティティ（IDが自動設定される）
     */
    void insert(TitleEntity titleEntity);

    /**
     * タイトルを更新します
     * @param titleEntity 更新するタイトルエンティティ
     */
    void update(TitleEntity titleEntity);

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

    /**
     * タイトルを検索します
     * @param query 検索クエリ (null の場合は条件なし)
     * @param watchStatus 視聴状態でのフィルタリング (null の場合は条件なし)
     * @return 条件にマッチしたタイトルエンティティリスト
     */
    List<TitleEntity> search(@Param("query") String query, @Param("watchStatus") WatchStatus watchStatus);

    /**
     * タイトルIDでタイトル情報URLを取得します
     * @param titleId タイトルID
     * @return タイトル情報URLのリスト
     */
    List<com.example.videowatchlog.domain.model.TitleInfoUrl> selectTitleInfoUrlsByTitleId(@Param("id") Long titleId);
}
