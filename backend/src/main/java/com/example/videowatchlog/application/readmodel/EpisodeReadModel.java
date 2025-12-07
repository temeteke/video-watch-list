package com.example.videowatchlog.application.readmodel;

import com.example.videowatchlog.domain.model.WatchStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * EpisodeReadModel - エピソード用の読み取りモデル
 *
 * Phase 7 CQRS パターン: Read Model
 * - Episode + ViewingRecord の情報を保持
 * - SeriesReadModel に含まれる
 */
public class EpisodeReadModel {
    private final Long id;
    private final Long seriesId;
    private final String episodeInfo;
    private final WatchStatus watchStatus;
    private final List<String> watchPageUrls;
    private final List<ViewingRecordReadModel> viewingRecords;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public EpisodeReadModel(Long id, Long seriesId, String episodeInfo, WatchStatus watchStatus,
                           List<String> watchPageUrls, List<ViewingRecordReadModel> viewingRecords,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.seriesId = seriesId;
        this.episodeInfo = episodeInfo;
        this.watchStatus = watchStatus;
        this.watchPageUrls = watchPageUrls;
        this.viewingRecords = viewingRecords;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public String getEpisodeInfo() {
        return episodeInfo;
    }

    public WatchStatus getWatchStatus() {
        return watchStatus;
    }

    public List<String> getWatchPageUrls() {
        return watchPageUrls;
    }

    public List<ViewingRecordReadModel> getViewingRecords() {
        return viewingRecords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
