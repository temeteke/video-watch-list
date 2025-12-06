package com.example.videowatchlog.domain.service;

/**
 * IdService - エンティティID采番の基本仕様
 *
 * ドメインサービス：すべてのエンティティの一意識別子采番を提供します。
 * 各エンティティごとに専用の実装を持つことで、
 * 責任を明確に分離し、将来的なカスタマイズに対応できます。
 */
public interface IdService {
    /**
     * 新規エンティティのIDを生成します
     *
     * @return 采番されたID
     */
    Long generateId();
}
