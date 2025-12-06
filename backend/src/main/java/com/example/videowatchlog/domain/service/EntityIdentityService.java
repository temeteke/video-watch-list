package com.example.videowatchlog.domain.service;

/**
 * EntityIdentityService - エンティティID採番サービス
 *
 * ドメインサービス：すべてのエンティティの一意識別子採番を提供します。
 * 複数のエンティティで共通のID生成戦略（PostgreSQL シーケンス）を使用します。
 */
public interface EntityIdentityService {
    /**
     * 新規エンティティのIDを生成します
     *
     * @return 採番されたID
     */
    Long generateId();
}
