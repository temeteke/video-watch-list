package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;

/**
 * TitleListReadModel - タイトル一覧表示用の読み取りモデル
 *
 * Phase 7 CQRS パターン: Read Model
 * - Title 単体の情報を保持
 * - Series/Episode 情報は含まない（一覧表示では不要）
 * - SELECT 最適化用モデル
 */
public class TitleListReadModel {
    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TitleListReadModel(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
