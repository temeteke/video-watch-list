package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SeriesReadModel - シリーズ用の読み取りモデル
 *
 * Phase 7 CQRS パターン: Read Model
 * - Series + Episode の情報を保持
 * - TitleDetailReadModel に含まれる
 */
public class SeriesReadModel {
    private final Long id;
    private final Long titleId;
    private final String name;
    private final List<EpisodeReadModel> episodes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public SeriesReadModel(Long id, Long titleId, String name, List<EpisodeReadModel> episodes,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.titleId = titleId;
        this.name = name;
        this.episodes = episodes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTitleId() {
        return titleId;
    }

    public String getName() {
        return name;
    }

    public List<EpisodeReadModel> getEpisodes() {
        return episodes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
