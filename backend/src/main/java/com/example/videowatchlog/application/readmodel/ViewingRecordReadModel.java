package com.example.videowatchlog.application.readmodel;

import java.time.LocalDateTime;

/**
 * ViewingRecordReadModel - 視聴記録用の読み取りモデル
 *
 * Phase 7 CQRS パターン: Read Model
 */
public class ViewingRecordReadModel {
    private final Long id;
    private final Long episodeId;
    private final LocalDateTime watchedAt;
    private final Integer rating;
    private final String comment;
    private final LocalDateTime recordedAt;

    public ViewingRecordReadModel(Long id, Long episodeId, LocalDateTime watchedAt,
                                 Integer rating, String comment, LocalDateTime recordedAt) {
        this.id = id;
        this.episodeId = episodeId;
        this.watchedAt = watchedAt;
        this.rating = rating;
        this.comment = comment;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getEpisodeId() {
        return episodeId;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
