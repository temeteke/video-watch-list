package com.example.videowatchlog.infrastructure.persistence.entity;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchPageUrl;
import com.example.videowatchlog.domain.model.WatchStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Episode entity for persistence layer (MyBatis mapping).
 * This class is responsible for database persistence only.
 * Business logic resides in domain.model.Episode.
 */
public class EpisodeEntity {
    private Long id;
    private Long seriesId;
    private String episodeInfo;
    private WatchStatus watchStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Package-private default constructor for MyBatis.
     */
    EpisodeEntity() {
    }

    // Getters

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    public void setEpisodeInfo(String episodeInfo) {
        this.episodeInfo = episodeInfo;
    }

    public void setWatchStatus(WatchStatus watchStatus) {
        this.watchStatus = watchStatus;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Convert to domain model (Episode).
     * Related entities (watchPageUrls, viewingRecords) are loaded separately by the repository.
     *
     * @param watchPageUrls Watch page URLs (loaded separately)
     * @param viewingRecords Viewing records (loaded separately)
     * @return Episode domain model
     */
    public Episode toDomain(List<WatchPageUrl> watchPageUrls, List<ViewingRecord> viewingRecords) {
        return new Episode(
            this.id,
            this.seriesId,
            this.episodeInfo,
            watchPageUrls != null ? watchPageUrls : new ArrayList<>(),
            this.watchStatus,
            viewingRecords != null ? viewingRecords : new ArrayList<>(),
            this.createdAt,
            this.updatedAt
        );
    }

    /**
     * Create from domain model (Episode).
     * Note: watchPageUrls and viewingRecords are not included (managed separately).
     *
     * @param episode Domain model
     * @return EpisodeEntity for persistence
     */
    public static EpisodeEntity fromDomain(Episode episode) {
        EpisodeEntity entity = new EpisodeEntity();
        entity.setId(episode.getId());
        entity.setSeriesId(episode.getSeriesId());
        entity.setEpisodeInfo(episode.getEpisodeInfo());
        entity.setWatchStatus(episode.getWatchStatus());
        entity.setCreatedAt(episode.getCreatedAt());
        entity.setUpdatedAt(episode.getUpdatedAt());
        return entity;
    }
}
