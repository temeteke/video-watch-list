package com.example.videowatchlog.application.readmodel.repository;

import com.example.videowatchlog.application.readmodel.TitleDetailReadModel;
import com.example.videowatchlog.application.readmodel.TitleListReadModel;
import java.util.List;
import java.util.Optional;

/**
 * TitleReadRepository インターフェース
 *
 * CQRS Read Model用のリポジトリインターフェースです。
 * Application層に配置され、読み取り専用のクエリを提供します。
 *
 * Architecture Decision:
 * - Read Model用の Repository インターフェースを Application層に配置
 * - Infrastructure層の MyBatis Mapper への依存性を抽象化
 * - Application層のサービスがこのインターフェースに依存することで、
 *   Onion Architecture の原則（Application → Infrastructure の一方向依存）を遵守
 *
 * CQRS Principle:
 * - Command Side: Domain repositories (write operations)
 * - Query Side: This TitleReadRepository (read operations)
 */
public interface TitleReadRepository {
    /**
     * すべてのタイトル一覧を取得
     *
     * @return タイトル一覧
     */
    List<TitleListReadModel> findAllTitles();

    /**
     * タイトル詳細を取得（Series/Episode/ViewingRecord を含む）
     *
     * @param titleId タイトルID
     * @return タイトル詳細
     */
    Optional<TitleDetailReadModel> findTitleDetailById(Long titleId);
}
