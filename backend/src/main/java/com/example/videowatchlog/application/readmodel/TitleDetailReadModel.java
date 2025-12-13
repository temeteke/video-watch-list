package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TitleDetailReadModel - タイトル詳細表示用の読み取りモデル
 *
 * Phase 7 CQRS パターン: Read Model
 * - Title + Series + Episode の完全な階層構造を保持
 * - JOIN クエリで単一の SELECT で取得
 * - 詳細ビュー用の最適化モデル
 */
public class TitleDetailReadModel {
    private final Long id;
    private final String name;
    private final List<SeriesReadModel> series;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TitleDetailReadModel(Long id, String name, List<SeriesReadModel> series,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.series = series;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SeriesReadModel> getSeries() {
        return series;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
